package com.fintech.insurance.components.web.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

/**
 * @Description: 覆盖默认的异步线程池处理: 主要是为了把主线程的请求上下文传播到线程池中的子线程中去。
 * @Author: Yong Li
 * @Date: 2017/12/19 14:50
 */
@Service
public class FInsuranceAsyncConfigurer implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(2);
        threadPool.setMaxPoolSize(4);
        threadPool.setQueueCapacity(6);
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        threadPool.setAwaitTerminationSeconds(60 * 15);
        threadPool.setThreadNamePrefix("FInsuranceAsync-");
        threadPool.initialize();

        return new FInsuranceAsyncTaskExecutor(threadPool);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
