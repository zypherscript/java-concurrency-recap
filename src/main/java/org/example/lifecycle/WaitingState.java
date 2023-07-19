package org.example.lifecycle;

public class WaitingState implements Runnable {

  public static Thread t1;

  public static void main(String[] args) throws InterruptedException {
    t1 = new Thread(new WaitingState());
    t1.setName("threaddddddddd1");
    t1.start();

    Thread.sleep(1000);

    Thread.getAllStackTraces().keySet()
        .stream()
        .filter(stackTraceElements -> stackTraceElements.getName().contains("threaddddddddd"))
        .forEach(thread -> {
          System.out.println(thread.getName() + " :: " + thread.getState());
        });
  }

  public void run() {
    Thread t2 = new Thread(new DemoWaitingStateRunnable());
    t2.setName("threaddddddddd2");
    t2.start();

    try {
      t2.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
  }
}

class DemoWaitingStateRunnable implements Runnable {

  public void run() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }

    System.out.println(WaitingState.t1.getState());
  }
}
