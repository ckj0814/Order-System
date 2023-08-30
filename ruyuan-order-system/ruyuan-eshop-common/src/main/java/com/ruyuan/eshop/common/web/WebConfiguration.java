package com.ruyuan.eshop.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruyuan.eshop.common.core.DateProvider;
import com.ruyuan.eshop.common.core.DateProviderImpl;
import com.ruyuan.eshop.common.core.ObjectMapperImpl;
import com.ruyuan.eshop.common.exception.GlobalExceptionHandler;
import com.ruyuan.eshop.common.json.JsonExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * web相关bean组件配置
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Configuration
@Import(value = {GlobalExceptionHandler.class, GlobalResponseBodyAdvice.class})
public class WebConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImpl();
    }

    @Bean
    public DateProvider dateProvider() {
        return new DateProviderImpl();
    }

    @Bean
    public JsonExtractor jsonExtractor() {
        return new JsonExtractor();
    }
}