---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by lxy_m.
--- DateTime: 2019/12/17 14:46
---

-- Validate value before writing
-- 写入前验证值
function writeWithValidation(r,name,value)
    if(value >=1 and value <= 10) then
        if not aerospike:exists(r) then
            aerospike:create(r)
        end
        r[name] = value
        aerospike:update(r)
    else
        error("1000:Invalid value")
    end
end

-- Set a particular bin only if record does not already exists.
-- 仅在记录不存在时设置特定的bin
function writeUnique(r,name,value)
    if not aerospike:exists(r) then
        aerospike:create(r)
        r[name] = value
        aerospike:update(r)
    end
end

function readBin(r,name)
    return r[name]
end

function multiplyAndAdd(r,a,b)
    return r['age'] * a + b
end

local function one(rec)
    return 1
end

local function add(a, b)
    return a + b
end
--[[
count()应用于流（在这种情况下，来自查询的结果流）。我们将要对结果执行的操作添加到流中：

map—将值从流映射到另一个值。在示例中，映射定义为函数one()，该函数将一条记录映射到值1。
reduce—将流中的值缩减为单个值。在该示例中，通过将流中的两个值相加来执行归约，这两个值恰好是从map函数返回的1 。
最终结果是一个包含单个值的流：计数，或更严格地说，结果集中每个记录的总和为1。
]]--
function count(stream)
    return stream : map(one) : reduce(add);
end

local mm = require("exports")
function helloworld(rec)
    return mm.hello() .. mm.world()
end
