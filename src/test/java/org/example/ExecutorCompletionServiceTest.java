package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ExecutorCompletionServiceTest {

  private final static ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);

  @Test
  void simpleTest() throws InterruptedException, ExecutionException {
    CompletionService<String> service = new ExecutorCompletionService<>(WORKER_THREAD_POOL);

    List<Callable<String>> callables = Arrays.asList(
        new DelayedCallable("fast thread", 100),
        new DelayedCallable("slow thread", 3000));

    for (Callable<String> callable : callables) {
      service.submit(callable);
    }

    long startProcessingTime = System.currentTimeMillis();

    Future<String> future = service.take();
    String firstThreadResponse = future.get();
    long totalProcessingTime
        = System.currentTimeMillis() - startProcessingTime;

    assertEquals("fast thread", firstThreadResponse);
    assertTrue(totalProcessingTime >= 100
        && totalProcessingTime < 1000);
    System.out.println("Thread finished after: " + totalProcessingTime
        + " milliseconds");

    future = service.take();
    String secondThreadResponse = future.get();
    totalProcessingTime
        = System.currentTimeMillis() - startProcessingTime;

    assertEquals("slow thread", secondThreadResponse);
    assertTrue(
        totalProcessingTime >= 3000
            && totalProcessingTime < 4000);
    System.out.println("Thread finished after: " + totalProcessingTime
        + " milliseconds");

    awaitTerminationAfterShutdown(WORKER_THREAD_POOL);
  }

  private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
    threadPool.shutdown();
    try {
      if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
        threadPool.shutdownNow();
      }
    } catch (InterruptedException ex) {
      threadPool.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

}

class DelayedCallable implements Callable<String> {

  private final String name;
  private final long period;
  private CountDownLatch latch;

  public DelayedCallable(String name, long period, CountDownLatch latch) {
    this(name, period);
    this.latch = latch;
  }

  public DelayedCallable(String name, long period) {
    this.name = name;
    this.period = period;
  }

  public String call() {

    try {
      Thread.sleep(period);

      if (latch != null) {
        latch.countDown();
      }

    } catch (InterruptedException ex) {
      // handle exception
      ex.printStackTrace();
      Thread.currentThread().interrupt();
    }

    return name;
  }
}
