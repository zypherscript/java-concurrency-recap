package org.example.utilconcurrent;

import java.util.concurrent.Semaphore;

public class LoginQueueUsingSemaphore {

  private final Semaphore semaphore;

  public LoginQueueUsingSemaphore(int slotLimit) {
    semaphore = new Semaphore(slotLimit);
  }

  boolean tryLogin() {
    return semaphore.tryAcquire();
  }

  void logout() {
    semaphore.release();
  }

  int availableSlots() {
    return semaphore.availablePermits();
  }

}
