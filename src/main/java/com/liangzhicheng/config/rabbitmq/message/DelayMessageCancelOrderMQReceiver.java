package com.liangzhicheng.config.rabbitmq.message;

import com.liangzhicheng.modules.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@RabbitListener(queues = "test.order.cancel")
@Component
public class DelayMessageCancelOrderMQReceiver {

    @Resource
    private IOrderService orderService;

    @RabbitHandler
    public void handle(Long orderId){
        log.info("[订单服务] receive delay message ... orderId：{}", orderId);
        orderService.cancel(orderId);
    }

}
