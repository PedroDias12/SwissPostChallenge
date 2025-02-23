package com.example.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
public class ThreadConfig {

    @Bean
    public Executor taskExecutor() {
        int maxNumberOfThreads = 3;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(maxNumberOfThreads);
        executor.setMaxPoolSize(maxNumberOfThreads);
        executor.setThreadNamePrefix("AssetPriceUpdaterTaskExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("SchedulerTaskExecutor-");
        return scheduler;
    }
}
