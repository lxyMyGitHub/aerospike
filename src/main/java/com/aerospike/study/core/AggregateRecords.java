package com.aerospike.study.core;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Language;
import com.aerospike.client.lua.LuaConfig;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.ResultSet;
import com.aerospike.client.query.Statement;
import com.aerospike.study.util.AerospikeConnectionPool;

/**
 * @ClassName AggregateRecords 聚合,汇总
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 9:47
 * @Version 1.0
 */
public class AggregateRecords {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
    public static void main(String[] args) {
        AerospikeClient client = clientPool.getClient();
        /**
         * SELECT count(*)
         * FROM test.demo
         * WHERE d = 50
         * 在Aerospike中，您可以使用UDF和查询来执行此操作。
         */
        Statement stmt = new Statement();
        stmt.setNamespace("test");
        stmt.setSetName("lxy_test001");
//        stmt.setFilters( Filter.range("age", 0,100) );
        stmt.setBinNames("age");
//        RecordSet rs = client.query(null, stmt);
//        while (rs.next()){
//            System.out.println(rs.getKey().toString());
//            System.out.println(rs.getRecord().toString());
//        }
        //定义流UDF
        //要计算满足查询条件的记录数，请定义一个UDF以计算所有返回的记录。
        // 查询执行时，将产生结果流。
        // 查询记录说明，该流包含可以使用客户端API进行迭代的记录。
        // 但是，Aerospike提供了使用Stream UDF处理结果流的功能。
        // 流UDF允许使用处理流过流的数据的操作来扩展流。
        client.register(null, "D:\\workspace\\idea_workspace\\AerospikeStudy\\src\\main\\java\\com\\aerospike\\study\\resources\\example.lua", "example.lua", Language.LUA);
        //配置UDF搜索路径
        /**
         * 对于客户端流UDF处理，必须将客户端指向UDF模块的本地位置。
         * 使用com.aerospike.client.lua.LuaConfig以下命令定义UDF模块的位置
         */
        LuaConfig.SourceDirectory = "D:\\workspace\\idea_workspace\\AerospikeStudy\\src\\main\\java\\com\\aerospike\\study\\resources\\";
        //执行查询
        //要执行查询并处理结果，请使用AerospikeClient.queryAggregate()：
        /**
         * public class AerospikeClient {
         *     public final ResultSet queryAggregate(QueryPolicy policy,
         *         Statement statement,
         *         String packageName,
         *         String functionName,
         *         Value... functionArgs
         *     ) throws AerospikeException
         * }
         */
        ResultSet rs = client.queryAggregate(null,stmt,"example","count");
        //处理结果
        //该查询产生一个com.aerospike.client.query.ResultSet，使您可以迭代聚合的结果：
        if(rs.next()){
            Object result = rs.getObject();
            System.out.println("Count is : "+ result.toString());
        }
        rs.close();

    }
}
