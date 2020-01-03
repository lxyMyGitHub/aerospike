package com.aerospike.study.core.InitData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class AsInitThread {
    private final static int REGION_SIZE = 10000;
    private final static int THREAD_SIZE = 10;
    private final static boolean THREAD_SLEEP = false;
    private static LinkedList<String> region = getRegion(REGION_SIZE,1,1000000);
    private static List<Integer> result = new ArrayList<Integer>();
    
    
    
    
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for(int i = 1;i<=THREAD_SIZE;i++) {
            Thread countThread = new Thread(new CountThread(result,i,THREAD_SLEEP));
            countThread.setName("countThread"+i);
            System.out.println(countThread.getName()+"is ready");
            countThread.start();
        }
        while(Thread.activeCount()>1) {
            System.out.println("current region size is"+getRegionSize());
            System.out.println("current thread number is"+Thread.activeCount());
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("1~1000000之间的素数共有："+countResult()+"个");
        long endTime = System.currentTimeMillis();
        System.out.println("耗时："+(endTime-startTime)+"ms");
    }
    
    private static int countResult() {
        int count = 0;
        for (Integer i : result) {
            count +=i;
        }
        return count;
    }
    
    public static void setResult(int res) {
        result.add(res);
    }
    public static String getRegion() {
        return region.pop();
    }
    public static int getRegionSize() {
        return region.size();
    }
    
    
    
    private static LinkedList<String> getRegion(int regionSize,int minNumber,int maxNumber){
        if(minNumber>=maxNumber) {
            throw new RuntimeException("number is Illegal...");
        }
        LinkedList<String> result = new LinkedList<String>();
        int startNum = minNumber;
        int endNum= 0;
        while (true){
            endNum=startNum+regionSize;
            if(endNum<maxNumber) {
                result.add(startNum+"&&"+endNum);
                startNum = endNum+1;
            }else {
                result.add(startNum+"&&"+maxNumber);
                break;
            }
        }
        return result;
    }
    
    
    
    public static class CountThread implements Runnable{
        private List<Integer> result;
        private String threadName;
        private boolean sleepFlag;
        public CountThread(List<Integer> result,int nameId,boolean sleepFlag) {
            this.result=result;
            this.threadName = "countThread-"+nameId;
            this.sleepFlag=sleepFlag;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    if(getRegionSize()>0) {
                        System.out.println(threadName+" is run");
                        int count = 0;
                        String regionItem = getRegion();
                        int startNum = Integer.valueOf(regionItem.split("&&")[0]);
                        int endNum = Integer.valueOf(regionItem.split("&&")[1]);
                        for(int i = startNum;i<=endNum;i++) {
                            if(currentIsPrimeNum(i)) {
                                count++;
                            }
                        }
                        setResult(count);
                        System.out.println(threadName+"   : "+startNum+"-"+endNum+" count successful"); 
                    }else {
                        if(sleepFlag) {
                            Thread.sleep(500);
                        }else {
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //
        private boolean currentIsPrimeNum (int number) {
            boolean flag = true;
            for(int i=2;i<number;i++) {
                if(number%i==0) {
                    flag=false;
                    break;
                }
            }
            return flag;
        }
    }
}
