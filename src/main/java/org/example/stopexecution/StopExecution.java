package org.example.stopexecution;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class StopExecution {

  public static void main(String[] args) {
//    testUsingLoop();
//    testUsingTimer();
//    testExecutor();
    testScheduledExecutor();
  }

  public static void testUsingLoop() {
    System.out.println("using loop started");
    long start = System.currentTimeMillis();
    long end = start + 30 * 1000; // 30 seconds
    while (System.currentTimeMillis() < end) {
      System.out.println("running task");
      new FixedTimeTask(7 * 1000).run(); // 7 seconds
    }
    System.out.println("using loop ended");
  }

  public static void testUsingTimer() {
    System.out.println("using timer started");
    Thread thread = new Thread(new LongRunningTask());
    thread.start();

    Timer timer = new Timer();
    TimeOutTask timeOutTask = new TimeOutTask(thread, timer);

    System.out.println("scheduling timeout in 3 seconds");
    timer.schedule(timeOutTask, 3000);
    System.out.println("using timer ended");
  }

  public static void testExecutor() {
    final ExecutorService service = Executors.newSingleThreadExecutor();
    Future<String> f = null;
    try {
      f = service.submit(() -> {
        // Do you long running calculation here
        try {
          Thread.sleep(2737); // Simulate some delay
        } catch (InterruptedException e) {
          System.out.println("Interrupted");
          return "interrupted";
        }
        System.out.println("Sleep finished");
        return "42";
      });

      System.out.println(f.get(2, TimeUnit.SECONDS));
    } catch (TimeoutException e) {
      f.cancel(true);
      System.out.println("Calculation took to long");
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      service.shutdown();
    }
  }

  public static void testScheduledExecutor() {
    System.out.println("testScheduledExecutor");
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    Future<?> future = executor.submit(new LongRunningTask());
    executor.schedule(() -> {
      future.cancel(true);
    }, 1000, TimeUnit.MILLISECONDS);
    executor.shutdown();
  }
}
