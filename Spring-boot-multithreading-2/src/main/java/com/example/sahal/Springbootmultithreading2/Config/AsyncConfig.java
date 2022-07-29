package com.example.sahal.Springbootmultithreading2.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig{

    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(3000);
        executor.setThreadNamePrefix("EmployeeThread");
        executor.initialize();
        return executor;
    }
}
