package com.aerospike.study.core;

import com.aerospike.client.*;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.study.util.AerospikeConnectionPool;

/**
 * @ClassName ScanRecords 扫描记录
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 10:37
 * @Version 1.0
 */
public class ScanRecords implements ScanCallback {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
    private int recordCount;
    public static void main(String[] args) {
        Log.Callback myLog = new Logging();
        Log.setCallback(myLog);
        Log.setLevel(Log.Level.DEBUG);
        //Aerospike Java客户端可以扫描指定名称空间和集合中的所有记录
        new ScanRecords().runTest();
    }

    private  void runTest(){
        AerospikeClient client = clientPool.getClient();
        ScanPolicy policy = new ScanPolicy();
        //使用线程并行查询节点以获取记录；否则，顺序查询每个节点。
        policy.concurrentNodes = true;
        policy.priority = Priority.LOW;
        //仅返回指定的bins（或默认情况下返回所有bins）
        policy.includeBinData = false;
        client.scanAll(policy,"test","lxy_test001",this);
        System.out.println("runTest Records " + recordCount);
        clientPool.close(client);
    }


    @Override
    public void scanCallback(Key key, Record record) throws AerospikeException {
        recordCount ++;
        System.out.println("in callback ...");
        if ((recordCount % 100000 == 0)){
            System.out.println("scanCallback Records : " + recordCount);
        }

    }
}
