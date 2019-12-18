package com.aerospike.study.core;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.aerospike.study.util.AerospikeConnectionPool;


/**
 * @ClassName QueryTest
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 9:07
 * @Version 1.0
 */
public class QueryTest {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();

    public static void main(String[] args) {
        AerospikeClient client = clientPool.getClient();
        Statement stmt = new Statement();
        stmt.setNamespace("test");
        stmt.setSetName("lxy_test001");
        //应用过滤器
        //要查询二级索引，请在查询中指定一个过滤器。似乎允许使用多个过滤器，但是服务器当前将查询限制为单个过滤器：
        /**
         * public final class Statement {
         *     void setFilters(Filter... filters);
         * }
         * 过滤器是使用中的静态方法创建的com.aerospike.client.query.Filter。以下是可用的过滤器：
         * Filter.equal(name, value)—过滤具有指定值（整数或字符串）的bin 名称的记录。
         * Filter.range(name, begin, end)—过滤具有名称范围在指定范围内（仅限整数）的bin 名称的记录。
         */
//        stmt.setFilter(Filter.range("name",0,4));
        stmt.setFilter(Filter.equal("name","XYLiang"));
        //Projecting Bins
        //查询可以指定要读取的bins
        /**
         * public final class Statement {
         *     void setBinNames(String... binNames);
         * }
         */
        stmt.setBinNames("name");
        //执行查询
        /**
         * 要执行查询，请调用AerospikeClient.query
         * public class com.aerospike.client.AerospikeClient {
         *     public final RecordSet query(
         *         QueryPolicy policy,
         *         Statement statement
         *     )   throws AerospikeException;
         * }
         * policy—查询行为定义。默认值= null。
         * statement —查询执行。
         * RecordSet 始终返回，这使您可以迭代查询结果。
         */
        RecordSet res = client.query(null, stmt);
        try {
            while (res.next()){
                Key key = res.getKey();
                Record record = res.getRecord();
                System.out.println("key is " + key.toString());
                System.out.println("record is "+ record.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            res.close();
        }


    }
}
