package org.example.threadjoin;

public class SampleThread extends Thread {

  public int processingCount = 0;

  SampleThread(int processingCount) {
    this.processingCount = processingCount;
    System.out.println("Thread Created");
  }

  @Override
  public void run() {
    System.out.println("Thread " + this.getName() + " started");
    while (processingCount > 0) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("Thread " + this.getName() + " interrupted");
      }
      processingCount--;
      System.out.println(
          "Inside Thread " + this.getName() + ", processingCount = " + processingCount);
    }
    System.out.println("Thread " + this.getName() + " exiting");
  }

  public static void main(String[] args) throws InterruptedException {
    Thread t2 = new SampleThread(1);
    t2.start();
    System.out.println("Invoking join");
    t2.join();
    System.out.println("Returned from join");
    System.out.println(t2.isAlive());

    System.out.println("====================");

    Thread t1 = new SampleThread(0);
    t1.join();

    System.out.println("====================");

    Thread t3 = new SampleThread(10);
    t3.start();
    t3.join();
//    t3.join(1000);
    System.out.println(t3.isAlive());

    System.out.println("====================");

    SampleThread t4 = new SampleThread(10);
    t4.start();
    //not guaranteed to stop even if t4 finishes.
    do {
//      t4.join(100);
    } while (t4.processingCount > 0);
  }
}
