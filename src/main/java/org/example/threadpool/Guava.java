package org.example.threadpool;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Guava {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
//    Executor executor = Executors.newSingleThreadExecutor();
    Executor executor = MoreExecutors.directExecutor();

    AtomicBoolean executed = new AtomicBoolean();

    executor.execute(() -> {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      executed.set(true);
    });
    System.out.println(executed.get());
    System.out.println("=================");

    ThreadPoolExecutor executor2 =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    ExecutorService executorService =
        MoreExecutors.getExitingExecutorService(executor2,
            100, TimeUnit.MILLISECONDS);

    executorService.submit(() -> {
      while (true) {
      }
    });

    ExecutorService executorService2 = Executors.newCachedThreadPool();
    ListeningExecutorService listeningExecutorService =
        MoreExecutors.listeningDecorator(executorService2);

    ListenableFuture<String> future1 =
        listeningExecutorService.submit(() -> "Hello");
    ListenableFuture<String> future2 =
        listeningExecutorService.submit(() -> "World");

    String greeting = String.join(" ", Futures.allAsList(future1, future2).get());
    System.out.println(greeting);
  }
}
