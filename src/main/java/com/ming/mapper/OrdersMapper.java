package com.ming.mapper;

import com.ming.po.Orders;
import com.ming.po.User;

import java.util.List;

public interface OrdersMapper
{
    //延迟加载
    public List<Orders> findorderuserlazyload() throws Exception;
}
