package com.aerospike.study.core;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.study.util.AerospikeConnectionPool;

/**
 * @ClassName ErrorHanding
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 14:44
 * @Version 1.0
 * 错误处理
 */
public class ErrorHanding {
    /**
     * AerospikeException	基本异常。可以通过getResultCode（）获得更具体的错误代码。 查看结果代码
     * AerospikeException.Timeout	交易已超时。
     * AerospikeException.Serialize	使用Java或messagepack编码/解码时出错。
     * AerospikeException.Parse	解析服务器响应时出错。
     * AerospikeException.Connection	建立与服务器节点的连接时出错。
     * AerospikeException.InvalidNode	选择的节点不活动时发生错误。
     * AerospikeException.ScanTerminated	扫描过早终止。
     * AerospikeException.QueryTerminated	查询过早终止。
     * AerospikeException.CommandRejected	异步命令被拒绝，因为已超过最大并发数据库命令。
     */
    //样例
    private AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
    public void aerospikeExceptionTest() {
        AerospikeClient client = clientPool.getClient();
        try {
            try {
                client.put(null, new Key("test", "lxy_test001", "key"), new Bin("testBin", 32));
            } catch (AerospikeException.Timeout aet) {
                aet.printStackTrace();
            } catch (AerospikeException ae) {
                ae.printStackTrace();
            }
        }finally {
            clientPool.close(client);
        }
    }

    public static void main(String[] args) {
        new ErrorHanding().aerospikeExceptionTest();
    }
}
