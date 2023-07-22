package org.example.mutex;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Test {

  public static void getUniqueSequences(SequenceGenerator generator, int count)
      throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    Set<Integer> uniqueSequences = new LinkedHashSet<>();
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
  }
}
