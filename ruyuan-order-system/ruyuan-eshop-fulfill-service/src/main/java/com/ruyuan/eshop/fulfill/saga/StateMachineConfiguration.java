package com.ruyuan.eshop.fulfill.saga;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class StateMachineConfiguration {

    @Bean
    public ThreadPoolExecutorFactoryBean threadExecutor() {
        ThreadPoolExecutorFactoryBean threadExecutor = new ThreadPoolExecutorFactoryBean();
        threadExecutor.setThreadNamePrefix("SAGA_ASYNC_EXE_");
        threadExecutor.setCorePoolSize(1);
        threadExecutor.setMaxPoolSize(20);
        return threadExecutor;
    }

    @Bean
    public DbStateMachineConfig dbStateMachineConfig(ThreadPoolExecutorFactoryBean threadExecutor
            , DruidDataSource druidDataSource) throws IOException {
        DbStateMachineConfig dbStateMachineConfig = new DbStateMachineConfig();
        dbStateMachineConfig.setDataSource(druidDataSource);
        dbStateMachineConfig.setThreadPoolExecutor((ThreadPoolExecutor) threadExecutor.getObject());
        dbStateMachineConfig.setResources(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:statelang/*.json"));
        dbStateMachineConfig.setEnableAsync(true);
        dbStateMachineConfig.setTxServiceGroup("default_tx_group");
        return dbStateMachineConfig;
    }

    /**
     * saga状态机实例
     *
     * @param dbStateMachineConfig
     * @return
     */
    @Bean
    public ProcessCtrlStateMachineEngine stateMachineEngine(DbStateMachineConfig dbStateMachineConfig) {
        ProcessCtrlStateMachineEngine stateMachineEngine = new ProcessCtrlStateMachineEngine();
        stateMachineEngine.setStateMachineConfig(dbStateMachineConfig);
        return stateMachineEngine;
    }

    @Bean
    public StateMachineEngineHolder stateMachineEngineHolder(ProcessCtrlStateMachineEngine stateMachineEngine) {
        StateMachineEngineHolder stateMachineEngineHolder = new StateMachineEngineHolder();
        stateMachineEngineHolder.setStateMachineEngine(stateMachineEngine);
        return stateMachineEngineHolder;
    }

}
