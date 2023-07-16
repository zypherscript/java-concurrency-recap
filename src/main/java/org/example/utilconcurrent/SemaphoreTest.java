package org.example.utilconcurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class SemaphoreTest {

  static Semaphore semaphore = new Semaphore(10);

  public static void main(String[] args) {
    execute();
  }

  private static void execute() {
    int slots = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(slots);
    LoginQueueUsingSemaphore loginQueue = new LoginQueueUsingSemaphore(slots);
    IntStream.range(0, slots)
        .forEach(user -> executorService.execute(loginQueue::tryLogin));
    executorService.shutdown();

    System.out.println(loginQueue.availableSlots());
    System.out.println(loginQueue.tryLogin());

    loginQueue.logout();
    System.out.println(loginQueue.availableSlots());
    System.out.println(loginQueue.tryLogin());
    System.out.println(loginQueue.availableSlots());
  }
}
