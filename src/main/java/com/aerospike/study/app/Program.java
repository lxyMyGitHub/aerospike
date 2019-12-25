package com.aerospike.study.app;

import com.aerospike.client.AerospikeClient;
import com.aerospike.study.util.AerospikeConnectionPool;

/**
 * @ClassName Program
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/19 14:39
 * @Version 1.0
 */
public class Program {
    public static void main(String[] args) throws InterruptedException {
        AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
        AerospikeClient client = clientPool.getClient();
        UserService us = new UserService(client);
        TweetService ts = new TweetService(client);
//        us.createUser("jay","123456","f","1","1,2,3,4,5,6");
//        ts.createTweet("jay","鸡你太美");
//        us.getUser("jay");
        us.batchGetUserTweets("jay");

    }

}
