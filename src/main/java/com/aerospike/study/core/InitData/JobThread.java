package com.aerospike.study.core.InitData;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.study.util.AerospikeConnectionPool;
import com.aerospike.study.util.IdWorker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName JobThread
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2020/1/3 11:25
 * @Version 1.0
 */
public class JobThread implements Runnable {
    private String threadName ;
    private int dataNumber = 0;
    private AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
    public JobThread(String threadName,int dataNumber){
        this.threadName = threadName;
        this.dataNumber = dataNumber;
    }
    @Override
    public void run() {
        System.out.println(threadName + " :========: is run ...");
        AerospikeClient client = clientPool.getClient();
        IdWorker idWorker = new IdWorker(1,1);
        Random rd = new Random();
        for (int i = 0; i < dataNumber; i++) {
            String pkStr = idWorker.nextId() + "";
            log("pk is : "+ pkStr + "   items  : " + i);
            Key key = new Key("test","trade",pkStr);
            String merchantNumber = pkStr;
            int amount = rd.nextInt(10000);
            long tradeTime = System.currentTimeMillis();
            String cardType ="1";
            log("merchantNumber: "+merchantNumber+"  amount: "+amount+" threadName: "+threadName+" "+" cardType: "+cardType);
            Bin merchantNumberBin = new Bin("merchantNumber",merchantNumber);
            Bin amountBin = new Bin("amount",amount);
            Bin tradeTimeBin = new Bin("tradeTime",tradeTime);
            Bin cardTypeBin = new Bin("cardType",cardType);
            WritePolicy writePolicy = new WritePolicy();
            writePolicy.setTimeout(1000);
            client.put(writePolicy,key,merchantNumberBin,amountBin,tradeTimeBin,cardTypeBin);
        }
        clientPool.close(client);
        log(threadName+" is end ..");
    }


    private void log (String line){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + " " + threadName + " :======: " + line.toString());
    }
}
