package org.example.countdownlatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class WaitingWorker implements Runnable {

  private final List<String> outputScraper;
  private final CountDownLatch readyThreadCounter;
  private final CountDownLatch callingThreadBlocker;
  private final CountDownLatch completedThreadCounter;

  WaitingWorker(final List<String> outputScraper, final CountDownLatch readyThreadCounter,
      final CountDownLatch callingThreadBlocker, CountDownLatch completedThreadCounter) {

    this.outputScraper = outputScraper;
    this.readyThreadCounter = readyThreadCounter;
    this.callingThreadBlocker = callingThreadBlocker;
    this.completedThreadCounter = completedThreadCounter;
  }

  @Override
  public void run() {
    // Mark this thread as read / started
    readyThreadCounter.countDown();
    try {
      callingThreadBlocker.await();
      outputScraper.add("Counted down");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      completedThreadCounter.countDown();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
    CountDownLatch readyThreadCounter = new CountDownLatch(5);
    CountDownLatch callingThreadBlocker = new CountDownLatch(1);
    CountDownLatch completedThreadCounter = new CountDownLatch(5);
    List<Thread> workers = Stream
        .generate(() -> new Thread(new WaitingWorker(
            outputScraper, readyThreadCounter, callingThreadBlocker, completedThreadCounter)))
        .limit(5)
        .toList();

    workers.forEach(Thread::start);
    readyThreadCounter.await();
    outputScraper.add("Workers ready");
    callingThreadBlocker.countDown();
    completedThreadCounter.await();
    outputScraper.add("Workers complete");

    System.out.println(outputScraper);
  }
}
