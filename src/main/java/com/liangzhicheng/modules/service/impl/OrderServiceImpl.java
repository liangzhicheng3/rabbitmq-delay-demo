package com.liangzhicheng.modules.service.impl;

import com.liangzhicheng.config.rabbitmq.message.DelayMessageCancelOrderMQSender;
import com.liangzhicheng.modules.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    private DelayMessageCancelOrderMQSender delayMessageCancelOrderMQSender;

    @Override
    public String create() {
        log.info("[订单服务] create order start ...");
        //TODO 执行创建订单前置操作
        //下单完成后发送延迟消息，用于当用户没有支付时取消订单
        this.sendDelayMessageCancelOrder(1L);
        return "下单成功";
    }

    @Override
    public void cancel(Long orderId) {
        //TODO 执行取消订单前置操作
        log.info("[订单服务] cancel order ... orderId：{}", orderId);
    }

    private void sendDelayMessageCancelOrder(Long orderId) {
        //获取订单超时时间，假设为30分钟（测试用10秒）
        Integer delayTime = 10 * 1000;
        //发送延迟消息
        delayMessageCancelOrderMQSender.asyncSend(orderId, delayTime);
    }

}
