package com.aerospike.study.core;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Language;
import com.aerospike.client.Value;
import com.aerospike.client.task.RegisterTask;
import com.aerospike.study.util.AerospikeConnectionPool;

/**
 * @ClassName UDFTest
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/17 14:59
 * @Version 1.0
 */
public class UDFTest {
    private static AerospikeConnectionPool clientPool = new AerospikeConnectionPool();
    public static void main(String[] args) {
        AerospikeClient client = clientPool.getClient();
        registerUDFFormFile(client,"D:\\workspace\\idea_workspace\\AerospikeStudy\\src\\main\\java\\com\\aerospike\\study\\resources\\example.lua");
        runUdf(client);
    }
    public static void runUdf(AerospikeClient client){

        /**
         * 要调用Record UDF，请使用AerospikeClient.execute()：
         * public class AerospikeClient {
         *     public final Object execute(
         *         Policy policy,
         *         Key key,
         *         String packageName,
         *         String functionName,
         *         Value... args
         *     )   throws AerospikeException
         * }
         * key —在其上调用功能的记录的键。
         * packageName —包含要调用的函数的UDF模块。
         * functionName —要调用的函数。
         * args —函数参数。
         */
        Key key = new Key("test","lxy_test001","key006");
//        String result = client.execute(null,key,"example","readBin", Value.get("name")).toString();
        String result = client.execute(null,key,"example","multiplyAndAdd",Value.get(10),Value.get(5)).toString();
        System.out.println(result);

    }

    private static void registerUDFFormFile(AerospikeClient client,String fileUrl) {
        RegisterTask task = client.register(null, fileUrl, "example.lua", Language.LUA);
        //每秒完成一次轮询群集，最多10秒钟。
        task.waitTillComplete(1000,10000);
    }

    public static void registerUDF(AerospikeClient client){
        String newline = "\n";
        String code =
                "-- Validate value before writing." + newline +
                        "function writeWithValidation(r,name,value)" + newline +
                        "    if (value >= 1 and value <= 10) then" + newline +
                        "      if not aerospike:exists(r) then" + newline +
                        "        aerospike:create(r)" + newline +
                        "      end" + newline +
                        "      r[name] = value" + newline +
                        "      aerospike:update(r)" + newline +
                        "    else" + newline +
                        "        error(\"1000:Invalid value\")" + newline +
                "    end" + newline +
                "end" + newline +
                newline +
                "-- Set a particular bin only if record does not already exist." + newline +
                "function writeUnique(r,name,value)" + newline +
                "    if not aerospike:exists(r) then" + newline +
                "        aerospike:create(r)" + newline +
                "        r[name] = value" + newline +
                "        aerospike:update(r)" + newline +
                "    end" + newline +
                "end" + newline;

        RegisterTask task = client.registerUdfString(null, code, "example.lua", Language.LUA);
// Poll cluster for completion every second for a maximum of 10 seconds.
        task.waitTillComplete(1000, 10000);
    }
}
