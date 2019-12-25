package com.aerospike.study.app;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.study.util.AerospikeConnectionPool;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName InitDB
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/19 9:16
 * @Version 1.0
 */
public class InitDB {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();

    public static void main(String[] args) {
        int lowKeyVal =  0;
        int numKeys = 100000;
        if(args.length > 2 || args.length == 1){
            System.err.println("Invalid number or arguments");
            System.exit(1);
        }else if(args.length == 2){
            try {
                lowKeyVal = Integer.parseInt(args[0]);
                numKeys = Integer.parseInt(args[1]);
            }catch (NumberFormatException e){
                System.err.println("Both arguments must be integers");
                System.exit(1);
            }
        }
        AerospikeClient client = clientPool.getClient();
        int randomInt;
        Key key = null;
        long startTime = System.currentTimeMillis();
        for (int i = lowKeyVal; i < lowKeyVal + numKeys; i++) {
            key = new Key("test","demo",i);
            randomInt = ThreadLocalRandom.current().nextInt(1,100000);
            Bin int_bin = new Bin("intbin",randomInt);
            Bin str_bin = new Bin("strbin",String.valueOf(randomInt));
            client.put(new WritePolicy(),key,int_bin,str_bin);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("cast time is : "+ (endTime - startTime) + " ms");
        clientPool.close(client);
    }
}
