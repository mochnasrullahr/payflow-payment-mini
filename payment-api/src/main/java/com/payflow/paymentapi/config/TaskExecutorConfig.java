package com.payflow.paymentapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TaskExecutorConfig {

    /**
     * Simple fixed thread pool for payment processing.
     * Tweak pool size to match expected throughput.
     */
    @Bean("paymentExecutor")
    public ExecutorService paymentExecutor() {
        // production: consider ThreadPoolExecutor with bounded queue + metrics
        return Executors.newFixedThreadPool(4);
    }
}
