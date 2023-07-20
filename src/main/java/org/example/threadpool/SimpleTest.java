package org.example.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleTest {

  public static void main(String[] args) throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    Future<String> future = executorService.submit(() -> "Hello World");
    try {
      String result = future.get();
      System.out.println(result);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    executorService.shutdown();
    System.out.println("=================");

    ThreadPoolExecutor executor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    executor.submit(() -> {
      Thread.sleep(1000);
      return null;
    });
    executor.submit(() -> {
      Thread.sleep(1000);
      return null;
    });
    executor.submit(() -> {
      Thread.sleep(1000);
      return null;
    });

    System.out.println(executor.getPoolSize());
    System.out.println(executor.getQueue().size());

    executor.shutdown();
    System.out.println("=================");

    AtomicInteger counter = new AtomicInteger();

    ExecutorService executor2 = Executors.newSingleThreadExecutor();
    executor2.submit(() -> {
      counter.set(1);
    });
    executor2.submit(() -> {
      counter.compareAndSet(1, 2);
      System.out.println(counter.get());
      System.out.println("=================");
    });
    executor2.shutdown();

    ScheduledExecutorService executor3 = Executors.newScheduledThreadPool(5);
    executor3.schedule(() -> {
      System.out.println("Hello World");
      System.out.println("=================");
    }, 500, TimeUnit.MILLISECONDS);
    executor3.shutdown();

    CountDownLatch lock = new CountDownLatch(3);

    ScheduledExecutorService executor4 = Executors.newScheduledThreadPool(5);
    ScheduledFuture<?> future2 = executor4.scheduleAtFixedRate(() -> {
      System.out.println("Hello World");
      lock.countDown();
    }, 500, 100, TimeUnit.MILLISECONDS);

    lock.await(1000, TimeUnit.MILLISECONDS);
    future2.cancel(true);
    executor4.shutdown();
  }
}
