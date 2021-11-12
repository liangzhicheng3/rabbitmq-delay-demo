package com.liangzhicheng.common.enums;

import lombok.Getter;

@Getter
public enum QueueEnum {

    /**
     * 消息通知队列
     */
    QUEUE_ORDER_CANCEL("test.order.direct", "test.order.cancel", "test.order.cancel"),

    /**
     * 消息通知ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("test.order.direct.ttl", "test.order.cancel.ttl", "test.order.cancel.ttl");

    /**
     * 交换机名称
     */
    private String exchange;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String queueName, String routeKey) {
        this.exchange = exchange;
        this.queueName = queueName;
        this.routeKey = routeKey;
    }

}
