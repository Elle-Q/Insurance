package com.fintech.insurance.components.web.async;

import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/19 14:41
 */
public class FInsuranceAsyncTaskExecutor implements AsyncTaskExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(FInsuranceAsyncTaskExecutor.class);

    private AsyncTaskExecutor executor;

    public FInsuranceAsyncTaskExecutor( AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task, RequestContextHolder.getRequestAttributes(),
                FInsuranceApplicationContext.currentContext()), startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(createWrappedRunnable(task, RequestContextHolder.getRequestAttributes(),
                FInsuranceApplicationContext.currentContext()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(createWrappedRunnable(task, RequestContextHolder.getRequestAttributes(),
                FInsuranceApplicationContext.currentContext()));
    }

    private <T> Callable<T> createCallable(final Callable<T> task) {
        return new Callable<T>() {
            public T call() throws Exception {
                try {
                    return task.call();
                } catch (Exception ex) {
                    handleAsyncException(ex);
                    throw ex;
                }
            }
        };
    }

    private Runnable createWrappedRunnable(final Runnable task, final RequestAttributes requestAttributes, final Map<String, Object> jwtContext) {
        return new Runnable() {
            public void run() {
                // 把源Controller里面Reqeust属性和FInsuranceApplicationContext都拷贝到子线程中
                RequestContextHolder.setRequestAttributes(requestAttributes);
                FInsuranceApplicationContext.JWT_CONTEXT.set(jwtContext);
                try {
                    task.run();
                } catch (Exception ex) {
                    handleAsyncException(ex);
                }
            }
        };
    }

    private void handleAsyncException(Exception e) {
        LOG.error("Async service encounter exception, " + e.getMessage());
    }
}
