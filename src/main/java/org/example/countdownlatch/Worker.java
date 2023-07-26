package org.example.countdownlatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class Worker implements Runnable {

  private final List<String> outputScraper;
  private final CountDownLatch countDownLatch;

  Worker(final List<String> outputScraper, final CountDownLatch countDownLatch) {
    this.outputScraper = outputScraper;
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void run() {
    // Do some work
    System.out.println("Doing some logic");
    outputScraper.add("Counted down");
    countDownLatch.countDown();
  }

  public static void main(String[] args) throws InterruptedException {
    List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
    CountDownLatch countDownLatch = new CountDownLatch(5);
    List<Thread> workers = Stream
        .generate(() -> new Thread(new Worker(outputScraper, countDownLatch)))
        .limit(5)
        .toList();

    workers.forEach(Thread::start);
    countDownLatch.await();
    outputScraper.add("Latch released");

    System.out.println(outputScraper);
  }
}
