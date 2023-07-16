package org.example.utilconcurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTask implements Runnable {

  private final CyclicBarrier barrier;

  public CyclicBarrierTask(CyclicBarrier barrier) {
    this.barrier = barrier;
  }

  @Override
  public void run() {
    try {
      System.out.println(Thread.currentThread().getName() +
          " is waiting");
      barrier.await();
      System.out.println(Thread.currentThread().getName() +
          " is released");
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
  }

}
