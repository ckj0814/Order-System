package com.ruyuan.eshop.order.config;

import com.alibaba.fastjson.JSON;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
@Aspect
public class LogAopConfig {

    private static Logger logger = LoggerFactory.getLogger(LogAopConfig.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void apiLogAop() {

    }

    @Around("apiLogAop()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        String declaringTypeName = point.getSignature().getDeclaringTypeName();
        String signatureName = point.getSignature().getName();
        String argStr = argsToString(point.getArgs());
        logger.info(LoggerFormat.build()
                .remark("开始调用")
                .data("declaringTypeName", declaringTypeName)
                .data("signatureName", signatureName)
                .data("argStr", argStr)
                .finish());
        Object response = null;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = null;
        Long interval = null;

        try {
            //执行该方法
            response = point.proceed();
        } catch (Exception e) {
            logger.info(LoggerFormat.build()
                    .remark("请求异常")
                    .finish(), e);
            endTime = LocalDateTime.now();
            interval = Duration.between(startTime, endTime).toMillis();
            logger.info(LoggerFormat.build()
                    .remark("结束请求")
                    .data("declaringTypeName", declaringTypeName)
                    .data("signatureName", signatureName)
                    .data("响应时间", interval + "毫秒")
                    .finish());
            throw e;
        }
        endTime = LocalDateTime.now();
        interval = Duration.between(startTime, endTime).toMillis();
        logger.info(LoggerFormat.build()
                .remark("结束请求")
                .data("declaringTypeName", declaringTypeName)
                .data("signatureName", signatureName)
                .data("响应时间", interval + "毫秒")
                .data("响应内容", argsToString(response))
                .finish());

        return response;
    }

    private String argsToString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            logger.error("args to json error", e);
        }
        return String.valueOf(object);
    }

}
