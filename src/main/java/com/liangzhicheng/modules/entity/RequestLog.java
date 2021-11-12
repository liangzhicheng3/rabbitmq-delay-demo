package com.liangzhicheng.modules.entity;

import lombok.Data;

@Data
public class RequestLog {

    /**
     * 操作描述
     */
    private String description;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 请求返回的结果
     */
    private Object result;

    /**
     * 消耗时间
     */
    private Integer consumeTime;

    /**
     * 操作时间
     */
    private Long operateTime;

    /**
     * url
     */
    private String url;

}
