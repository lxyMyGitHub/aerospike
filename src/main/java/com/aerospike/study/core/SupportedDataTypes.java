package com.aerospike.study.core;

/**
 * @ClassName SupportedDataTypes
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 14:30
 * @Version 1.0
 * String
 * Integer
 * Blob
 * Map
 * List
 * 在Java中设置值时，
 * Aerospike库会自动确定用于存储的最佳本机Aerospike数据类型
 * 整数和长整数将转换为内部数字格式。
 * 字符串将转换为UTF-8。
 * 字节数组存储为Blob。
 * 为了避免格式转换，Java byte[]类型存储为纯blob，并以字节的形式提供给所有其他语言。
 * Aerospike通过Java序列化系统传递任何其他类型（例如复杂的Java集合类型），并将其简化为Java Blob
 */
public class SupportedDataTypes {
}
