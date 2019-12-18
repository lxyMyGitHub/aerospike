package com.aerospike.study.core.AsyncPackage;
import com.aerospike.client.async.EventLoops;
import com.aerospike.client.async.EventPolicy;
import com.aerospike.client.async.NettyEventLoops;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @ClassName EventLoops
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 16:01
 * @Version 1.0
 * 创建事件循环
 */
public class EventLoopsTest {
    /**
     * 创建将处理异步命令的事件循环。
     * 每个EventLoops实例可以在多个AerospikeClient实例之间共享。
     * EventLoops实例的事件循环数应近似于计算机上为AerospikeClient处理保留的cpu核心数。
     * Netty和直接NIO事件循环均受支持。
     */


    public static void main(String[] args) {
        //EventPolicy
        //每个事件循环都配置有一个EventPolicy实例。
        EventPolicy eventPolicy = new EventPolicy();
        //Event Loop Throttle
        /**
         * 默认情况下，异步命令会立即在事件循环上执行。
         * 如果在事件循环上执行的命令的速率始终超过事件循环可以处理这些命令的速率，则事件循环可能会过载。
         * 这可能会导致客户端占用过多的sockets ，从而对性能产生负面影响。
         * 在极端情况下，应用程序可能会用光套接字。
         * 这些EventPolicy的默认设置假定用户将应用外部限制来减轻事件循环的过载。
         * 示例AsyncTest通过向事件循环添加N个命令并在命令完成后再运行一个命令来执行此限制
         * 如果用户未应用外部限制，则设置EventPolicy.maxCommandsInProcess限制事件循环中允许的并发命令数非常重要。
         * 多余的命令将放置在没有分配套接字连接的延迟队列上，直到命令插槽可用为止。
         * 当插槽可用时，将为该命令分配套接字连接，并在事件循环上执行该命令。
         */

        //创建Netty事件循环
        /**
         * Netty允许用户与AerospikeClient共享他们现有的事件循环，从而提高性能。
         * 使用TLS连接时，也需要Netty事件循环。
         * Netty是可选的外部库依赖项
         */
        EventLoopGroup group = new NioEventLoopGroup(4);
        EventLoops eventLoops = new NettyEventLoops(eventPolicy,group);

    }
}
