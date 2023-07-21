package org.example.runnablecallable;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EventLoggingTask implements Runnable {

  @Override
  public void run() {
    System.out.println("Message");
  }

  public static void main(String[] args) {
    var executorService = Executors.newSingleThreadExecutor();
    Future<?> future = executorService.submit(new EventLoggingTask());
    executorService.shutdown();
  }
}
