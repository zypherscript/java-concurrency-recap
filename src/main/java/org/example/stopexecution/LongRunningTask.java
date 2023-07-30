package org.example.stopexecution;

public class LongRunningTask implements Runnable {

  @Override
  public void run() {
    System.out.println("running");
    for (int i = 0; i < Long.MAX_VALUE; i++) {
      if (Thread.interrupted()) {
        System.out.println("stopping");
        return;
      }
    }
    System.out.println("finished");
  }
}
