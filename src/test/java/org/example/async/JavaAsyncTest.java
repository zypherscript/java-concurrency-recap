package org.example.async;

import static org.example.async.JavaAsync.factorial;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;

class JavaAsyncTest {


  @Test
  void testThread() {
    int number = 20;
    Thread newThread = new Thread(() -> {
      System.out.println("Factorial of " + number + " is: " + factorial(number));
    });
    newThread.start();
    System.out.println("test");
  }

  @Test
  void testFutureTask() throws ExecutionException, InterruptedException {
    ExecutorService threadpool = Executors.newCachedThreadPool();
    Future<Long> futureTask = threadpool.submit(() -> factorial(20));

    while (!futureTask.isDone()) {
      System.out.println("FutureTask is not finished yet...");
    }
    long result = futureTask.get();
    System.out.println(result);

    threadpool.shutdown();
  }

  @Test
  void testCompletableFuture() throws ExecutionException, InterruptedException {
//    CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(
//        () -> factorial(20));
//    while (!completableFuture.isDone()) {
//      System.out.println("CompletableFuture is not finished yet...");
//    }
//    long result = completableFuture.get();
//    System.out.println(result);

    CompletableFuture.supplyAsync(() -> factorial(20))
        .thenAccept(System.out::println);
  }

  @Test
  void testListenableFuture() throws ExecutionException, InterruptedException {
    ExecutorService threadpool = Executors.newCachedThreadPool();
    ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
    ListenableFuture<Long> guavaFuture = (ListenableFuture<Long>) service.submit(
        () -> factorial(20));
    long result = guavaFuture.get();
    System.out.println(result);
  }
}