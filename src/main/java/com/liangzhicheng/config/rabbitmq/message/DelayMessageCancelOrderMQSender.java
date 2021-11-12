package com.liangzhicheng.config.rabbitmq.message;

import com.liangzhicheng.common.enums.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class DelayMessageCancelOrderMQSender {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void asyncSend(Long orderId, Integer delayTime){
        log.info("[订单服务] send delay message ... orderId：{}", orderId);
        amqpTemplate.convertAndSend(
                QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
                QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(),
                orderId,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        //设置延迟时间
                        message.getMessageProperties().setExpiration(String.valueOf(delayTime));
                        return message;
                    }
                });
    }

}
