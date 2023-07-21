package org.example.waitvssleep;

public class WaitSleepExample {

  private static final Object LOCK = new Object();

  public static void main(String... args) throws InterruptedException {
    sleepWaitInSynchronizedBlocks();
  }

  private static void sleepWaitInSynchronizedBlocks() throws InterruptedException {
    Thread.sleep(1000); // called on the thread
    System.out.println(
        "Thread '" + Thread.currentThread().getName() + "' is woken after sleeping for 1 second");

    synchronized (LOCK) {
      LOCK.wait(1000); // called on the object, synchronization required
      System.out.println("Object '" + LOCK + "' is woken after waiting for 1 second");
    }
  }
}
