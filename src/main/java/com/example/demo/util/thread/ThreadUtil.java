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
            count.addAndGet(1);
            thread.setName(MyThreadFactory.class.getName() + count);
            return thread;
        }
    }


    /**
     * 自定义的拒绝策略实现类  可以做异常记录,报警处理
     */
    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

        /**
         * 当执行此方法时,线程池和队列都满了,这里就是处置再新来的任务
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

        /*
         * shutdownNow()
         *  1.该方法返回尚未执行的task的List
         *  2.线程池的状态变为STOP状态
         *  3.阻止所有正在等待启动的任务,并且停止当前正在执行的任务
         *
         * shutdown()
         *  1.调用之后不允许继续往线程池内继续添加线程
         *  2.线程池的状态变为SHUTDOWN状态
         *  3.所有在调用shutdown()方法之前提交到 ExecutorService的任务都会执行
         *  4.所有线程结束执行当前任务,ExecutorService才会真正关闭
         *
         * 总结:
         *  shutdown()调用后,不可以再submit新的task,已经submit的将继续执行
         *  shutdownNow()调用后,试图停止当前正在执行的task,并返回尚未执行的task的list
         */
        executorService.shutdown();


        // 让主线程一直等待到线程池里面的线程都把任务执行完了才放行
        while (true) {
            // 判断线程池关闭后所有的任务是否都执行完了,注意这个方法只有在shutdown或shutdownNow方法调用后才有效
            if (executorService.isTerminated()) {
                // 结束线程
                return;
            }
        }
    }


    /**
     * 关闭线程池 等待一段时间并强行关闭当前正在执行中的任务,并忽略未执行的任务 (用来处理执行时间较长的任务)
     *
     * @param executorService 当前线程池
     * @param waitTime        等待时间
     * @param timeUnit        时间单位
     */
    public static void exit(ExecutorService executorService, long waitTime, TimeUnit timeUnit) {
        executorService.shutdown();
        try {
            // 等待指定时间后如果所有任务都执行完了返回true(等待指定时间后还有任务在执行中或者有任务提交了未执行)
            if (!executorService.awaitTermination(waitTime, timeUnit)) {
                // 强制结束正在执行的线程任务,返回尚未执行的task的list
                executorService.shutdownNow();
//                List<Runnable> runnableList = executorService.shutdownNow();
//                for (Runnable runnable : runnableList) {
//                    // 让它继续执行
//                    runnable.run();
//                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            e.printStackTrace();
        }
    }


}
