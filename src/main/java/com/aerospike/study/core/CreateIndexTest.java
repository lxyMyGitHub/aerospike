package com.aerospike.study.core;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.task.IndexTask;
import com.aerospike.study.util.AerospikeConnectionPool;

/**
 * @ClassName CreateIndexTest
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/17 15:47
 * @Version 1.0
 */
public class CreateIndexTest {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();

    public static void main(String[] args) {

        AerospikeClient client = clientPool.getClient();
        createIndex(client);
//        dropIndex(client);
    }

    /**
     * 要创建二级索引，请调用AerospikeClient.createIndex()。
     * 二级索引是异步创建的，因此该方法在二级索引传播到集群之前返回。
     * 作为一种选择，客户端可以等待异步服务器任务完成。
     * public class AerospikeClient {
     *     public final IndexTask createIndex(
     *         Policy policy,
     *         String namespace,
     *         String setName,
     *         String indexName,
     *         String binName,
     *         IndexType indexType
     *     )   throws AerospikeException
     * }
     * 创建二级索引
     * show indexes namespace
     * @param client
     */
    public static void createIndex(AerospikeClient client){
        IndexTask task = client.createIndex(null, "test", "lxy_test001","idx_test_lxy_test001_age", "age", IndexType.NUMERIC);
        task.waitTillComplete();
    }

    /**
     * 删除二级索引
     * 使用AerospikeClient.dropIndex()以下命令删除二级索引：
     * public class AerospikeClient {
     *     public final void dropIndex(
     *         Policy policy,
     *         String namespace,
     *         String setName,
     *         String indexName
     *     )   throws AerospikeException
     * }
     * @param client
     */
    public static void dropIndex(AerospikeClient client){
        IndexTask task = client.dropIndex(null, "test", "lxy_test001", "idx_test_lxy_test001_name");
        task.waitTillComplete();
    }

}
