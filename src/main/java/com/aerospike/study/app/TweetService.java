package com.aerospike.study.app;

import com.aerospike.client.*;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import java.util.Date;

/**
 * @ClassName TweetService
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/19 14:10
 * @Version 1.0
 */
public class TweetService {
    private AerospikeClient client;

    public TweetService(AerospikeClient client){
        this.client = client;
    }

    public void createTweet(String username,String tweet) throws AerospikeException,InterruptedException{
        System.out.println("\n********** Create Tweet **********\n");
        Record userRecord = null;
        Key userKey = null;
        Key tweetKey = null;

        // get username
        System.out.println("username is : " + username);
        if(username != null && username.length() > 0){
            userKey = new Key("test","users",username);
            userRecord = client.get(null,userKey);
            if(userRecord != null){
                int nextTweetCount = Integer.parseInt(userRecord.getValue("tweetcount").toString())+1;
                System.out.println("tweet content is : " + tweet);
                WritePolicy writePolicy = new WritePolicy();
                writePolicy.recordExistsAction = RecordExistsAction.UPDATE;
                long ts = new Date().getTime();
                tweetKey = new Key("test","tweets",username+":"+nextTweetCount);
                Bin tweetBin = new Bin("tweet",tweet);
                Bin tsBin = new Bin("ts",ts);
                Bin unameBin = new Bin("username",username);
                client.put(writePolicy,tweetKey,tweetBin,tsBin,unameBin);
                System.out.println("INFO: Tweet record created!");
                updateUser(client,userKey,writePolicy,ts,nextTweetCount);

            }else {
                System.out.println("ERROR: User record not found!");
            }
        }
    }

    private void updateUser(AerospikeClient client, Key userKey, WritePolicy writePolicy, long ts, int nextTweetCount) {
        Record record = client.get(null, userKey);
        Bin tweetcountBin = new Bin("tweetcount",nextTweetCount);
        client.put(writePolicy,userKey,tweetcountBin);
    }
}
