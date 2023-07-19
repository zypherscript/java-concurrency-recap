package org.example.volatilekw;

public class SharedObject {

  private volatile int count = 0;

  void incrementCount() {
    count++;
  }

  public int getCount() {
    return count;
  }

  public static void main(String[] args) throws InterruptedException {
    SharedObject sharedObject = new SharedObject();

    Thread writer = new Thread(sharedObject::incrementCount);
    writer.start();
    Thread.sleep(100);

    Thread writerTwo = new Thread(sharedObject::incrementCount);
    writerTwo.start();
    Thread.sleep(100);

    Thread readerOne = new Thread(() -> {
      int valueReadByThread2 = sharedObject.getCount();
      System.out.println(valueReadByThread2);
    });
    readerOne.start();

    Thread readerTwo = new Thread(() -> {
      int valueReadByThread3 = sharedObject.getCount();
      System.out.println(valueReadByThread3);
    });
    readerTwo.start();
  }
}
