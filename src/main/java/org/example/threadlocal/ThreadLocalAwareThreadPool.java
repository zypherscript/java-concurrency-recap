package org.example.threadlocal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareThreadPool extends ThreadPoolExecutor {

  public ThreadLocalAwareThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit, BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  @Override
  protected void beforeExecute(Thread thread, Runnable task) {
    // Perform any initialization or setup before executing the task
    // If using ThreadLocal, set the initial state here
  }

  @Override
  protected void afterExecute(Runnable task, Throwable throwable) {
    try {
      // Perform any cleanup after executing the task
      // Clear the ThreadLocal to avoid memory leaks
      // If using ThreadLocal, remove the data here
    } finally {
      super.afterExecute(task, throwable);
    }
  }

  // You can add other constructors or methods as per your requirement
}