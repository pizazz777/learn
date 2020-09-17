package com.example.demo.util.thread;

import javax.annotation.Nonnull;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/15
 * @description 线程工具类  建议使用ThreadPoolExecutor创建线程池,不推荐用Executors来创建
 */
public class ThreadUtil {

    private ThreadUtil() {
    }

    // 获取CPU核数
    private static final int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors();
    // 最大线程数一般设为2N+1最好,N是CPU核数
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = DEFAULT_CORE_SIZE * 2 + 1;
    private static final int DEFAULT_DEQUE_SIZE = 10;
    private static final long DEFAULT_KEEP_ALIVE_TIME = 1000L;

    /**
     * 拒绝策略  当线程池满并且队列也满了之后触发
     * {@link ThreadPoolExecutor.DiscardPolicy#DiscardPolicy()}: 直接抛弃任务
     * {@link ThreadPoolExecutor.AbortPolicy#AbortPolicy()}: 拒绝并抛出异常(默认)
     * {@link ThreadPoolExecutor.DiscardOldestPolicy#DiscardOldestPolicy()}: 移除等待队头任务,当前任务入队
     * {@link ThreadPoolExecutor.CallerRunsPolicy#CallerRunsPolicy()}: 直接调用任务run方法,那个线程是空闲的就会去执行任务,包括main线程
     * executor.prestartAllCoreThreads(); 预启动所有核心线程
     */
    private static final RejectedExecutionHandler DEFAULT_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    public static ExecutorService defaultThreadPool() {
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
     * {@link Executors#newCachedThreadPool()}
     *
     * @param maximumPoolSize 线程池最大容量
     * @return {@link ThreadPoolExecutor}
     */
    public static ExecutorService newCachedThreadPool(int maximumPoolSize) {
        maximumPoolSize = defaultIoPoolSize(maximumPoolSize);
        return new ThreadPoolExecutor(0,
                maximumPoolSize,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                DEFAULT_HANDLER);
    }

    /**
     * {@link Executors#newCachedThreadPool()}
     *
     * @return {@link ThreadPoolExecutor}
     */
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0,
                DEFAULT_MAXIMUM_POOL_SIZE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                DEFAULT_HANDLER);
    }


    /**
     * 定制的线程池
     */
    public static ExecutorService customThreadPool() {
        return new ThreadPoolExecutor(
                DEFAULT_CORE_SIZE,
                DEFAULT_MAXIMUM_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(DEFAULT_DEQUE_SIZE),
                new MyThreadFactory(),
                new MyRejectedExecutionHandler());
    }


    /**
     * 自定义的线程工厂实现类,创建线程
     */
    static class MyThreadFactory implements ThreadFactory {

        private AtomicInteger count = new AtomicInteger(0);

        /**
         * 自定义创建线程  设置线程名称等
         */
        @Override
        public Thread newThread(@Nonnull Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(MyThreadFactory.class.getName() + count);
            return thread;
        }
    }


    /**
     * 自定义的拒绝策略实现类  可以做异常记录,报警处理
     */
    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

        /**
         * 当执行此方法时,线程池满和队列都满了,这里就是处置再新来的任务
         *
         * @param r        the runnable task requested to be executed
         * @param executor the executor attempting to execute this task
         * @throws RejectedExecutionException if there is no remedy
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            // 将任务添加到队列中,但是要保证队列还有位置
            // executor.execute(r);

            // 直接执行任务 那个线程是空闲的就去执行,包括main线程(先执行了这个,才会再去执行队列里的任务)
            if (!executor.isShutdown()) {
                r.run();
            }
        }

    }


    /**
     * 默认IO最大线程池大小
     */
    private static int defaultIoPoolSize(int maximumPoolSize) {
        if (maximumPoolSize < 1) {
            maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;
        }
        return maximumPoolSize;
    }


    /**
     * 关闭线程池 等待所有的任务执行完毕
     *
     * @param executorService 当前线程池
     */
    public static void exit(ExecutorService executorService) {
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                return;
            }
        }
    }


}
