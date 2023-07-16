package org.example.utilconcurrent;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

  public static void main(String[] args) {
//    execute();
//    executeTask();
    start();
    
  }

  private static void execute() {
    Executor executor = new Invoker();
    executor.execute(() -> {
      System.out.println(Thread.currentThread().getName());
    });
  }

  private static void executeTask() {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    executor.submit(new Task());
//    executor.submit(() -> {
//      new Task();
//    });
    executor.shutdown();

    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        executor.shutdownNow();

        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
          System.err.println("Executor did not terminate.");
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }

  private static void start() {

    CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
      System.out.println("All previous tasks are completed");
    });

    Thread t1 = new Thread(new CyclicBarrierTask(cyclicBarrier), "T1");
    Thread t2 = new Thread(new CyclicBarrierTask(cyclicBarrier), "T2");
    Thread t3 = new Thread(new CyclicBarrierTask(cyclicBarrier), "T3");

    if (!cyclicBarrier.isBroken()) {
      t1.start();
      t2.start();
      t3.start();
    }
  }
}
