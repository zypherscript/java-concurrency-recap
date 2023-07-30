package org.example.stopexecution;

import java.util.concurrent.TimeUnit;

public class FixedTimeTask implements Runnable {

  final int fixedTime; // milliseconds

  public FixedTimeTask(int fixedTime) {
    this.fixedTime = fixedTime;
  }

  @Override
  public void run() {
    System.out.println(fixedTime + " milliseconds running task");
    try {
      TimeUnit.MILLISECONDS.sleep(fixedTime);
    } catch (InterruptedException e) {
      System.out.println("interrupted");
    }
    System.out.println("finished");
  }
}
