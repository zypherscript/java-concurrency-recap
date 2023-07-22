package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadPoolTaskExecutorUnitTest {

  public void startThreads(ThreadPoolTaskExecutor taskExecutor, CountDownLatch countDownLatch,
      int numThreads) {
    for (int i = 0; i < numThreads; i++) {
      taskExecutor.execute(() -> {
        System.out.println("==");
        try {
          Thread.sleep(100L * ThreadLocalRandom.current().nextLong(1, 10));
//          Thread.sleep(2000);
          countDownLatch.countDown();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
    }
  }

  @Test
  public void whenUsingDefaults_thenSingleThread() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.afterPropertiesSet();

    CountDownLatch countDownLatch = new CountDownLatch(10);
    this.startThreads(taskExecutor, countDownLatch, 10);

    while (countDownLatch.getCount() > 0) {
      assertEquals(1, taskExecutor.getPoolSize());
    }
  }

  @Test
  public void whenCorePoolSizeFive_thenFiveThreads() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(5);
    taskExecutor.afterPropertiesSet();

    CountDownLatch countDownLatch = new CountDownLatch(10);
    this.startThreads(taskExecutor, countDownLatch, 10);

    while (countDownLatch.getCount() > 0) {
      assertEquals(5, taskExecutor.getPoolSize());
    }
  }

  @Test
  public void whenCorePoolSizeFiveAndMaxPoolSizeTen_thenFiveThreads() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(5);
    taskExecutor.setMaxPoolSize(10);
    taskExecutor.afterPropertiesSet();

    CountDownLatch countDownLatch = new CountDownLatch(10);
    this.startThreads(taskExecutor, countDownLatch, 10);

    while (countDownLatch.getCount() > 0) {
      assertEquals(5, taskExecutor.getPoolSize());
    }
  }

  @Test
  public void whenCorePoolSizeFiveAndMaxPoolSizeTenAndQueueCapacityTen_thenTenThreads() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(5);
    taskExecutor.setMaxPoolSize(10);
    taskExecutor.setQueueCapacity(10);
    taskExecutor.afterPropertiesSet();

    CountDownLatch countDownLatch = new CountDownLatch(20);
    this.startThreads(taskExecutor, countDownLatch, 20);

    while (countDownLatch.getCount() > 0) {
      assertEquals(10, taskExecutor.getPoolSize());
    }
  }

}
