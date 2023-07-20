package org.example.runnable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class SimpleRunnable implements Runnable {

  private final String message;

  public SimpleRunnable(String message) {
    this.message = message;
  }

  @Override
  public void run() {
    System.out.println(message);
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    Thread thread = new Thread(new SimpleRunnable(
        "SimpleRunnable executed using Thread"));
    thread.start();
    thread.join();

    var executorService = Executors.newSingleThreadExecutor();
    executorService.submit(new SimpleRunnable(
        "SimpleRunnable executed using ExecutorService")).get();
    executorService.shutdown();
  }
}
