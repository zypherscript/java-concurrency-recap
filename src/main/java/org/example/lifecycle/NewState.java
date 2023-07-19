package org.example.lifecycle;

public class NewState implements Runnable {

  public static void main(String[] args) throws InterruptedException {
    Runnable runnable = new NewState();
    Thread t = new Thread(runnable);
    System.out.println(t.getState());

    t.start();
    System.out.println(t.getState());

    Thread.sleep(5000);
    System.out.println(t.isAlive());
  }

  @Override
  public void run() {
    System.out.println("run");
  }
}
