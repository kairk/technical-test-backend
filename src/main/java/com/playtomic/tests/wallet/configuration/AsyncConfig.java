package com.playtomic.tests.wallet.configuration;

import com.playtomic.tests.wallet.exception.handler.WalletAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        //Here we can change executor properties (i.e: threadNamePrefix, maxPoolSize...)
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.initialize();
        return exec;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new WalletAsyncExceptionHandler();
    }

}
