package com.aerospike.study.app;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.study.util.AerospikeConnectionPool;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName QueryDB
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/19 10:35
 * @Version 1.0
 */
public class QueryDB {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
    public static void main(String[] args) {
        int lowKeyVal = 0;
        int numKeys = 100000;
        int numReads = 100000;

        if (args.length > 0 && args.length != 3) {
            System.err.println("Invalid number of arguments");
            System.exit(1);
        } else if (args.length == 3) {
            try {
                lowKeyVal = Integer.parseInt(args[0]);
                numKeys = Integer.parseInt(args[1]);
                numReads = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("All arguments must be integers");
                System.exit(1);
            }
        }
        AerospikeClient client = clientPool.getClient();
        long startTime = System.currentTimeMillis();
        int randomInt;
        Key key = null;
        for (int i = 0; i < numReads; i++) {
            randomInt = ThreadLocalRandom.current().nextInt(lowKeyVal, lowKeyVal + numKeys);
            key = new Key("test", "demo", randomInt);
            Record record = client.get(new Policy(),key);
            System.out.println("int:" + record.getInt("intbin") + " string: " + record.getString("strbin"));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("records read/sec: " + numReads/((endTime - startTime)/1000));
        clientPool.close(client);
    }
}
