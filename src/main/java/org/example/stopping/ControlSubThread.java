package org.example.stopping;

import java.util.concurrent.atomic.AtomicBoolean;

public class ControlSubThread implements Runnable {

  private Thread worker;
  private final int interval;
  private final AtomicBoolean running = new AtomicBoolean(false);
  private final AtomicBoolean stopped = new AtomicBoolean(true);


  public ControlSubThread(int sleepInterval) {
    interval = sleepInterval;
  }

  public void start() {
    worker = new Thread(this);
    worker.start();
  }

  public void stop() {
    running.set(false);
  }

  public void interrupt() {
    running.set(false);
    worker.interrupt();
  }

  boolean isRunning() {
    return running.get();
  }

  boolean isStopped() {
    return stopped.get();
  }

  public void run() {
    running.set(true);
    stopped.set(false);
    while (running.get()) {
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Thread was interrupted, Failed to complete operation");
      }
      // do something
    }
    stopped.set(true);
  }

  public static void main(String[] args) throws InterruptedException {
    int interval = 5;

    ControlSubThread controlSubThread = new ControlSubThread(interval);
    controlSubThread.start();

    // Give things a chance to get set up
    Thread.sleep(interval);
    System.out.println(controlSubThread.isRunning());
    System.out.println(controlSubThread.isStopped());

    // Stop it and make sure the flags have been reversed
    controlSubThread.stop();

    Thread.sleep(1000);
    System.out.println(controlSubThread.isStopped());

    System.out.println("=========================");
    int newInterval = 50;

    ControlSubThread newControlSubThread = new ControlSubThread(newInterval);
    newControlSubThread.start();

    // Give things a chance to get set up
    Thread.sleep(newInterval);
    System.out.println(newControlSubThread.isRunning());
    System.out.println(newControlSubThread.isStopped());

    // Stop it and make sure the flags have been reversed
    newControlSubThread.interrupt();

    Thread.sleep(1000);
    System.out.println(newControlSubThread.isStopped());
  }
}
