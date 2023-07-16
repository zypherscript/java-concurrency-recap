package org.example.utilconcurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScheduledExecutorServiceTest {

  public static void main(String[] args) {
    executeSchedule();
  }


  private static void executeSchedule() {
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    Future<String> future = executorService.schedule(() -> "Hello world", 1, TimeUnit.SECONDS);
//    ScheduledFuture<String> scheduledFuture = executorService.schedule(() -> "Hello world", 1,
//        TimeUnit.SECONDS);
//    scheduleAtFixedRate, scheduleWithFixedDelay

    try {
      String result = future.get(10, TimeUnit.SECONDS);
      System.out.println(result);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }

    executorService.shutdown();
  }

}
