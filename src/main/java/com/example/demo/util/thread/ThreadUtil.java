package com.example.demo.util.thread;

import java.util.concurrent.*;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/15
 * @description 线程工具类
 */
public class ThreadUtil {

    private ThreadUtil() {
    }

    private static final int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = DEFAULT_CORE_SIZE + 1;
    private static final int DEFAULT_DEQUE_SIZE = DEFAULT_CORE_SIZE + 1;
    private static final long DEFAULT_KEEP_ALIVE_TIME = 1000L;

    /**
     * 拒绝策略  当线程池满并且队列也满了之后,会触发拒绝策略
     * {@link ThreadPoolExecutor.DiscardPolicy#DiscardPolicy()}: 直接抛弃任务
     * {@link ThreadPoolExecutor.AbortPolicy#AbortPolicy()}: 拒绝并抛出异常(默认)
     * {@link ThreadPoolExecutor.DiscardOldestPolicy#DiscardOldestPolicy()}: 移除等待队头任务,当前任务入队
     * {@link ThreadPoolExecutor.CallerRunsPolicy#CallerRunsPolicy()}: 直接调用任务run方法
     */
    private static final RejectedExecutionHandler DEFAULT_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    public static ExecutorService defaultThreadPool() {
        // executor.prestartAllCoreThreads(); 预启动所有核心线程
        return new ThreadPoolExecutor(
                DEFAULT_CORE_SIZE,
                DEFAULT_MAXIMUM_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(DEFAULT_DEQUE_SIZE),
                Executors.defaultThreadFactory(),
                DEFAULT_HANDLER);
    }


    /**
     * 关闭线程池 等待所有的任务执行完毕
     *
     * @param executorService 当前线程池
     */
    public static void exit(ExecutorService executorService) {
        executorService.shutdown();
        for (; ; ) {
            if (executorService.isTerminated()) {
                return;
            }
        }
    }


}
