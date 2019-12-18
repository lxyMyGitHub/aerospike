package com.aerospike.study.core.AsyncPackage;

import com.aerospike.client.async.EventLoops;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.async.*;
import com.aerospike.client.listener.WriteListener;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.study.util.AerospikeConnectionPool;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName AsyncTest
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 14:59
 * @Version 1.0
 */
public final class AsyncTest {
    private AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
    private AerospikeClient client;
    private EventLoops eventLoops;
    private final Monitor monitor = new Monitor();
    private final AtomicInteger recordCount = new AtomicInteger();
    private final int recordMax = 100000;
    private final int writeTimeout = 5000;
    private final int eventLoopSize;
    private final int concurrentMax;

    public AsyncTest(){
        //为每个cpu内核分配一个事件循环。
        eventLoopSize = Runtime.getRuntime().availableProcessors();
        //每个事件循环允许40个并发命令。
        concurrentMax = eventLoopSize * 40;
    }

    public void runTest() throws AerospikeException{
        EventPolicy eventPolicy = new EventPolicy();
        eventPolicy.minTimeout = writeTimeout;
        //直接NIO(new IO)
        eventLoops = new NioEventLoops(eventPolicy,eventLoopSize);

        //Netty NIO
//        EventLoopGroup group = new NioEventLoopGroup(eventLoopSize);
//        eventLoops = new NettyEventLoops(eventPolicy,group);

        //Netty epoll (Linux only)
//        EventLoopGroup group = new NioEventLoopGroup(eventLoopSize);
//        eventLoops = new NettyEventLoops(eventPolicy,group);
        try{
            //现在可以使用分配的事件循环创建AerospikeClient实例。
            ClientPolicy clientPolicy = new ClientPolicy();
            clientPolicy.eventLoops = eventLoops;
            clientPolicy.maxConnsPerNode = concurrentMax;
            clientPolicy.writePolicyDefault.setTimeout(writeTimeout);
            /**
             * AerospikeClient仅需要一台主机即可为群集设置种子，
             * 但是仍建议使用所有已知主机作为种子，因为某些主机可能处于非活动状态。
             * AerospikeClient构造函数还接受一个主机数组。
             */
            /**
             * AerospikeClient可以在单个实例中执行同步和异步命令。
             * 执行异步命令时需要EventLoops。
             * AerospikeClient是线程安全的，可以同时使用
             */
            client = clientPool.getClient();

            try{
                writeRecords();
                monitor.waitTillComplete();
                System.out.println("Records written : " + recordCount.get());
            }finally {
                //在程序关闭之前，应关闭AerospikeClient和EventLoops。
                clientPool.close(client);
            }
        }finally {
            //AerospikeClient将等到待完成的异步命令完成后再关闭。
            // 在close（）之后发出的异步命令将被拒绝。
            eventLoops.close();
        }
    }

    private void writeRecords(){
        //确切地编写并发Max命令以播种事件循环。
        //在事件循环中分配种子命令。
        //在WriteListener中完成每个命令后，将启动一个新命令。

        for (int i = 1; i < concurrentMax; i++) {
            EventLoop eventLoop = eventLoops.next();
            writeRecord(eventLoop,new AWriteListener(eventLoop),i);
        }
    }
    private void writeRecord(EventLoop eventLoop, WriteListener listener, int keyIndex) {
        client = clientPool.getClient();
        Key key = new Key("test","test",keyIndex);
        Bin bin = new Bin("bin",keyIndex);
        //异步put()方法需要额外的事件循环并编写监听器参数。put()完成时将调用监听器
        //启动写入
        client.put(eventLoop,listener,null,key,bin);
        clientPool.close(client);
    }

    //完成处理程序
    //当数据库从write事务返回时，将调用onSuccess或onFailure。
    private class AWriteListener implements WriteListener{
        private final EventLoop eventLoop;

        public AWriteListener(EventLoop eventLoop){
            this.eventLoop = eventLoop;
        }

        @Override
        public void onSuccess(Key key) {
            try{
                int count = recordCount.incrementAndGet();
                //如果所有记录均已写入，则停止
                if(count >= recordMax){
                    monitor.notifyComplete();
                    return;
                }
                if(count % 10000 == 0){
                    System.out.println(" Records written : "+ count );
                }
                //如有必要，发出一个新命令。
                int keyIndex = concurrentMax + count;
                if(keyIndex <= recordMax){
                    //在同一事件循环上写下一条记录。
                    writeRecord(eventLoop,this,keyIndex);
                }

            }catch (Exception e){
                e.printStackTrace();
                monitor.notifyComplete();
            }
        }

        @Override
        public void onFailure(AerospikeException e) {
            e.printStackTrace();
            monitor.notifyComplete();
        }
    }

    public static void main(String[] args) {
        try {
            AsyncTest test = new AsyncTest();
            test.runTest();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
