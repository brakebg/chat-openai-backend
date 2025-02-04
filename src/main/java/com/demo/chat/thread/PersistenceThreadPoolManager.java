package com.demo.chat.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistenceThreadPoolManager {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceThreadPoolManager.class);
    private static final int SESSION_POOL_SIZE = 5;
    private static final int MESSAGE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 20;
    private static final long KEEP_ALIVE_TIME = 60L; // 60 seconds

    public static final ThreadPoolExecutor sessionExecutor = new ThreadPoolExecutor(
            SESSION_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000),
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    // Custom handling when queue is full
                    System.out.println("Task rejected: Queue is full");
                    // You can log this event, notify a monitoring system, or implement a fallback strategy
                    // For example, you could try to run the task in the current thread:
                    // r.run();
                }
            }
    );


    public static final ThreadPoolExecutor messageExecutor = new ThreadPoolExecutor(
            MESSAGE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    // Custom handling when queue is full
                    System.out.println("Task rejected: Queue is full");
                    // You can log this event, notify a monitoring system, or implement a fallback strategy
                    // For example, you could try to run the task in the current thread:
                    // r.run();
                }
            }
    );

    static {
        Runnable monitorTask = () -> {
            while (true) {
                logger.info("SessionExecutor Queue Size: " + sessionExecutor.getQueue().size());
                logger.info("MessageExecutor Queue Size: " + messageExecutor.getQueue().size());
                try {
                    Thread.sleep(600000); // Log every 1 min
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };
        Thread monitorThread = new Thread(monitorTask);
        monitorThread.setDaemon(true);
        monitorThread.start();
    }
}