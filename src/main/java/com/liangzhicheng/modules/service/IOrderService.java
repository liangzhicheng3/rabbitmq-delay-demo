package com.liangzhicheng.modules.service;

public interface IOrderService {

    String create();

    void cancel(Long orderId);

}
