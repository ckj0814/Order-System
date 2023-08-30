package com.ruyuan.eshop.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 默认的web mvc相关配置
 * </p>
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Configuration
public class DefaultWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalWebMvcInterceptor()).addPathPatterns("/**");
    }

}