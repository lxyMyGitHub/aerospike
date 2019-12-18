package com.aerospike.study.core;

/**
 * @ClassName AsyncAPI
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 14:51
 * @Version 1.0
 * 异步API
 * AerospikeClient提供异步方法，
 * 这些方法将事件循环和监听器回调作为额外的参数。
 * 异步方法将命令注册到事件循环并返回。
 * 事件循环线程将处理命令并将结果发送到监听器。
 * 优点:与同步方法相比，异步方法使用更少的线程，并更有效地使用线程。
 * 缺点:异步编程模型很难实现，调试和维护.
 */
public class AsyncAPI {
}
