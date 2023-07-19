package org.example.utilconcurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockExample {

  private static int counter = 0;
  private static final Lock lock = new ReentrantLock();

  public static void main(String[] args) {
    // Create multiple threads that increment the counter concurrently
    for (int i = 0; i < 5; i++) {
      Thread thread = new Thread(() -> {
        for (int j = 0; j < 100000; j++) {
          incrementCounter();
        }
      });
      thread.start();
    }

    // Wait for all threads to finish
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Print the final counter value
    System.out.println("Counter: " + counter);
  }

  private static void incrementCounter() {
    lock.lock(); // Acquire the lock
    try {
      counter++; // Increment the counter
    } finally {
      lock.unlock(); // Release the lock in a finally block to ensure it always gets released
    }
  }
}

