package com.aerospike.study.core;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Value;
import com.aerospike.client.task.ExecuteTask;
import com.aerospike.study.util.AerospikeConnectionPool;

/**
 * @ClassName Transforming 转换记录
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 9:26
 * @Version 1.0
 */

public class Transforming {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();

    public static void main(String[] args) {
        AerospikeClient client = clientPool.getClient();
        /**
         * UPDATE test.demo
         * SET a = 1, b = 2
         * WHERE c = 3
         * Aerospike提供了类似的功能，但是允许您通过在每个记录上应用Record UDF函数来转换记录。
         * 记录UDF应用于单个记录。它可以作为参数提供，并且可以读取和写入记录的bin并执行计算。
         * 请参阅 示例包QueryExecute中的示例。
         */

        /**
         * 定义记录UDF
         * 以下是在中processRecord定义的Record UDF record_example.lua：
         */

        //注册UDF
        //定义UDF功能后，它必须在Aerospike集群中注册。请参阅注册UDF。

        //初始化查询语句

        //Statement stmt = new Statement();
        //stmt.setNamespace(params.namespace);
        //stmt.setSetName(params.set);
        //stmt.setFilters(Filter.range(binName1, begin, end));

        //在记录上执行UDF
//        ExecuteTask task = client.execute(params.policy, stmt,"record_example", "processRecord", Value.get("binName1"), Value.get("binName2"), Value.get(100))
        /**
         * stmt —指定UDF的记录集。
         * record_example —指定UDF模块。
         * processRecord —指定要在每个记录上使用的函数。
         * 这些参数直通到UDF功能：name1，name2，和addValue。
         */

        //确定UDF处理状态
        //要通知应用程序作业状态，请waitTillComplete()定期调用任务。
        //task.waitTillComplete();

    }
}
