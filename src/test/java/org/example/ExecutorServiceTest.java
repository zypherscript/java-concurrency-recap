package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ExecutorServiceTest {


  @Test
  void testExecuteRunnable() {
    Runnable runnableTask = () -> {
      try {
        TimeUnit.MILLISECONDS.sleep(300);
        System.out.println("done");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };

    var executorService = Executors.newSingleThreadExecutor();
    executorService.execute(runnableTask);
    executorService.shutdown();
  }

  @Test
  void testSubmitCallables() throws InterruptedException, ExecutionException {
    Callable<String> callableTask = () -> {
      TimeUnit.MILLISECONDS.sleep(300);
      return "Task's execution";
    };

    List<Callable<String>> callableTasks = new ArrayList<>();
    callableTasks.add(callableTask);
    callableTasks.add(callableTask);
    callableTasks.add(callableTask);

    var executorService = Executors.newSingleThreadExecutor();
//    Future<String> future = executorService.submit(callableTasks);
    List<Future<String>> futures = executorService.invokeAll(callableTasks);
    for (Future<String> future : futures) {
      String s = future.get();
      System.out.println(s);
    }

    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
