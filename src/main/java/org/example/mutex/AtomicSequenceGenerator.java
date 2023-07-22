package org.example.mutex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicSequenceGenerator {

  private final AtomicInteger currentValue = new AtomicInteger();

  private int getNextSequence() {
    return currentValue.incrementAndGet();
  }

  private int getCurrentValue() {
    return currentValue.intValue();
  }

  private static void getUniqueSequences(AtomicSequenceGenerator generator, int count)
      throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    var uniqueSequences = new CopyOnWriteArraySet<Integer>();
    List<Future<Integer>> futures = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      futures.add(executor.submit(generator::getNextSequence));
    }

    for (Future<Integer> future : futures) {
      uniqueSequences.add(future.get());
    }

    executor.awaitTermination(1, TimeUnit.SECONDS);
    executor.shutdown();

    System.out.println(uniqueSequences.size());
    System.out.println(generator.getCurrentValue());
  }

  public static void main(String[] args) throws Exception {
    getUniqueSequences(new AtomicSequenceGenerator(), 1000);
  }
}
