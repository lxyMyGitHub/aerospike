package com.aerospike.study.app;

import com.aerospike.client.*;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import java.util.Arrays;

/**
 * @ClassName UserService
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/19 13:54
 * @Version 1.0
 */
public class UserService {
    private AerospikeClient client;
    public UserService(AerospikeClient client){
        this.client = client;
    }
    public UserService(){

    }

    /**
     *
     * @param username
     * @param password
     * @param gender
     * @param region
     * @param interests
     * @throws AerospikeException
     */
    public void createUser(String username,String password,String gender,String region,String interests) throws AerospikeException{
        System.out.println("\n********** Create User **********\n");
        System.out.println("username  :  " +username);
        System.out.println("password  :  " +password);
        System.out.println("gender  :  " +gender);
        System.out.println("region  :  " +region);
        System.out.println("interests  :  " +interests);
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.recordExistsAction = RecordExistsAction.UPDATE;

        Key key = new Key("test","users",username);
        Bin unameBin = new Bin("username",username);
        Bin pwdBin = new Bin("password",password);
        Bin genderBin = new Bin("gender",gender);
        Bin regionBin = new Bin("region",region);
        Bin lastBin = new Bin("lasttweeted",0);
        Bin tBin = new Bin("tweetcount",0);
        Bin interestsBin = new Bin("interests", Arrays.asList(interests.split(",")));
        client.put(writePolicy,key,unameBin,pwdBin,genderBin,regionBin,lastBin,tBin,interestsBin);
        System.out.println("\nINFO: User record created!");
    }

    public void getUser(String username) throws AerospikeException{
        Record userRecord = null;
        Key userKey = null;
        //get username
        System.out.println("username is : "+ username);
        if(username != null && username.length() > 0 ){
            userKey = new Key("test","users",username);
            userRecord = client.get(null,userKey);
            if(userRecord != null){
                System.out.println("username:   " + userRecord.getValue("username"));
                System.out.println("password:   " + userRecord.getValue("password"));
                System.out.println("gender:     " + userRecord.getValue("gender"));
                System.out.println("region:     " + userRecord.getValue("region"));
                System.out.println("tweetcount: " + userRecord.getValue("tweetcount"));
                System.out.println("interests:  " + userRecord.getValue("interests"));
            }else{
                System.out.println("ERROR: User record not found!");
            }
        }else {
            System.out.println("ERROR: User record not found!");
        }

    }

    public void batchGetUserTweets(String username) throws AerospikeException{
        Record userRecord = null;
        Key userKey = null;
        System.out.println("username is : " + username);
        if(username != null && username.length() > 0){
            userKey = new Key("test", "users", username);
            userRecord = client.get(null, userKey);
            if (userRecord != null) {
                int tweetCount = Integer.parseInt(userRecord.getValue("tweetcount").toString());
                Key[] keys = new Key[tweetCount];
                for (int i = 0; i < keys.length; i++) {
                    keys[i] = new Key("test", "tweets",
                            (username + ":" + (i + 1)));
                }
                System.out.println("Here's " + username + "'s tweet(s):");
                if (keys.length > 0){
                    Record[] records = client.get(new BatchPolicy(),keys);
                    for (int j = 0; j < records.length; j++) {
                        System.out.println(records[j].getValue("tweet").toString());
                    }
                }
            }
        }else {
            System.out.println("ERROR: User record not found!");
        }

    }
}
