package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class CyclicBarrierVsCountDownLatchTest {

  //CyclicBarrier maintains a count of threads whereas CountDownLatch maintains a count of tasks
  @Test
  void countDownLatchTest() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(2);
    Thread t = new Thread(() -> {
      countDownLatch.countDown();
      countDownLatch.countDown();
    });
    t.start();
    countDownLatch.await();

    assertEquals(0, countDownLatch.getCount());
  }

  @Test
  void CyclicBarrierTest() throws InterruptedException {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    Thread t = new Thread(() -> {
      try {
        cyclicBarrier.await();
        cyclicBarrier.await(); //this is useless. A single thread can't count down a barrier twice
      } catch (InterruptedException | BrokenBarrierException e) {
        // error handling
      }
    });
    t.start();

    Thread.sleep(1000);

    assertEquals(1, cyclicBarrier.getNumberWaiting());
    assertFalse(cyclicBarrier.isBroken());
  }

  //when the barrier trips in CyclicBarrier, the count resets to its original value. CountDownLatch is different because the count never resets.
  @Test
  void CountdownLatchReset() {
    AtomicInteger updateCount = new AtomicInteger();
    CountDownLatch countDownLatch = new CountDownLatch(7);
    ExecutorService es = Executors.newFixedThreadPool(20);
    for (int i = 0; i < 20; i++) {
      es.execute(() -> {
        long prevValue = countDownLatch.getCount();
        countDownLatch.countDown();
        if (countDownLatch.getCount() != prevValue) {
          updateCount.incrementAndGet();
        }
      });
    }
    es.shutdown();

    System.out.println(updateCount.intValue());
    assertTrue(updateCount.intValue() <= 7);
  }

  @Test
  void CyclicBarrierReset() throws InterruptedException {
    AtomicInteger updateCount = new AtomicInteger();
    CyclicBarrier cyclicBarrier = new CyclicBarrier(7);

    ExecutorService es = Executors.newFixedThreadPool(20);
    for (int i = 0; i < 20; i++) {
      es.execute(() -> {
        try {
//          System.out.println(cyclicBarrier.getNumberWaiting());
          if (cyclicBarrier.getNumberWaiting() > 0) {
            updateCount.incrementAndGet();
          }
          cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
          // error handling
        }
      });
    }
    es.shutdown();

    assertTrue(updateCount.intValue() > 7);
  }
}
