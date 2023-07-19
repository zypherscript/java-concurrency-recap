package org.example.utilconcurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SynchronizedMethods {

  private int sum = 0;
  static int staticSum = 0;

  public void calculate() {
    setSum(getSum() + 1);
  }

  public synchronized void synchronisedCalculate() {
    setSum(getSum() + 1);
  }

  public static synchronized void syncStaticCalculate() {
    staticSum = staticSum + 1;
  }

  public int getSum() {
    return sum;
  }

  public void setSum(int sum) {
    this.sum = sum;
  }

  public static void main(String[] args) throws InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(3);
    SynchronizedMethods summation = new SynchronizedMethods();

    IntStream.range(0, 1000)
        .forEach(count -> service.submit(summation::synchronisedCalculate));
    service.awaitTermination(1000, TimeUnit.MILLISECONDS);
    service.shutdownNow();

    System.out.println(summation.getSum());

    ExecutorService service2 = Executors.newCachedThreadPool();

    IntStream.range(0, 1000)
        .forEach(count -> service2.submit(SynchronizedMethods::syncStaticCalculate));
    service2.awaitTermination(1000, TimeUnit.MILLISECONDS);
    service2.shutdownNow();

    System.out.println(SynchronizedMethods.staticSum);
  }
}