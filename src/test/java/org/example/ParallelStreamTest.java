package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class ParallelStreamTest {

  @Test
  public void givenList_whenCallingParallelStream_shouldBeParallelStream() {
    List<Long> aList = new ArrayList<>();

    Stream<Long> parallelStream = aList.parallelStream();
    assertTrue(parallelStream.isParallel());

    Stream<Long> parallelStream2 = aList.stream().parallel();
    assertTrue(parallelStream2.isParallel());
  }

  @Test
  public void giveRangeOfLongs_whenSummedInParallel_shouldBeEqualToExpectedTotal()
      throws InterruptedException, ExecutionException {

    long firstNum = 1;
    long lastNum = 1_000_000;

    List<Long> aList = LongStream.rangeClosed(firstNum, lastNum).boxed().toList();

    ForkJoinPool customThreadPool = new ForkJoinPool(4);
    try {
      long actualTotal = customThreadPool.submit(
          () -> aList.parallelStream().reduce(0L, Long::sum)).get();

      assertEquals((lastNum + firstNum) * lastNum / 2, actualTotal);

    } finally {
      customThreadPool.shutdown();
    }

//    long actualTotal2 = aList.parallelStream().reduce(0L, Long::sum);
//    assertEquals((lastNum + firstNum) * lastNum / 2, actualTotal2);
  }
}
