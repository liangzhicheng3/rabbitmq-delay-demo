package com.liangzhicheng.modules.controller;

import com.liangzhicheng.common.basic.ResponseResult;
import com.liangzhicheng.modules.service.IOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @RequestMapping(value = "/create")
    public ResponseResult create(){
        return ResponseResult.success(orderService.create());
    }

}
