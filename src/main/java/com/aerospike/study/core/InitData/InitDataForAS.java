package com.aerospike.study.core.InitData;


/**
 * @ClassName InitDataForAS
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2020/1/3 11:06
 * @Version 1.0
 */
public class InitDataForAS {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 50 ;i++) {
            Thread initThread = new Thread(new JobThread("initThread - " + i,40000));
            initThread.setName("initThread - " + i);
            System.out.println(initThread.getName() + "is ready ! ! !");
            initThread.start();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗时："+(endTime-startTime)+"ms");
    }
}
