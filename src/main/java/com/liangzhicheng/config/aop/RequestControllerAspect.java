package com.liangzhicheng.config.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.liangzhicheng.modules.entity.RequestLog;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
@Order(1)
@Aspect
@Component
public class RequestControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestControllerAspect.class);

    @Pointcut("execution(public * com.liangzhicheng.modules.controller.*.*(..))")
    public void requestLog() {}

    @Before("requestLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {}

    @AfterReturning(value = "requestLog()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {}

    @Around("requestLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        RequestLog requestLog = new RequestLog();
        Object result = joinPoint.proceed();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            requestLog.setDescription(apiOperation.value());
        }
        long endTime = System.currentTimeMillis();
        String url = request.getRequestURL().toString();
        requestLog.setBasePath(StrUtil.removeSuffix(url, URLUtil.url(url).getPath()));
        requestLog.setIp(request.getRemoteUser());
        requestLog.setMethod(request.getMethod());
        requestLog.setParameter(this.getParameter(method, joinPoint.getArgs()));
        requestLog.setResult(result);
        requestLog.setConsumeTime((int) (endTime - startTime));
        requestLog.setOperateTime(startTime);
        requestLog.setUrl(request.getRequestURL().toString());
        log.info("请求操作记录：{}", JSONUtil.parse(requestLog));
        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     * @param method
     * @param args
     * @return Object
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }

}
