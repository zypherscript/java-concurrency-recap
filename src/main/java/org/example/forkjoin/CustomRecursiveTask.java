package org.example.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import org.example.forkjoin.util.PoolUtil;

public class CustomRecursiveTask extends RecursiveTask<Integer> {

  private final int[] arr;

  private static final int THRESHOLD = 20;

  public CustomRecursiveTask(int[] arr) {
    this.arr = arr;
  }

  @Override
  protected Integer compute() {
    if (arr.length > THRESHOLD) {
      return ForkJoinTask.invokeAll(createSubtasks())
          .stream()
          .mapToInt(ForkJoinTask::join)
          .sum();
    } else {
      return processing(arr);
    }
  }

  private Collection<CustomRecursiveTask> createSubtasks() {
    List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
    dividedTasks.add(new CustomRecursiveTask(
        Arrays.copyOfRange(arr, 0, arr.length / 2)));
    dividedTasks.add(new CustomRecursiveTask(
        Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
    return dividedTasks;
  }

  private Integer processing(int[] arr) {
    return Arrays.stream(arr)
        .filter(a -> a > 10 && a < 27)
        .map(a -> a * 10)
        .sum();
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    Random random = new Random();
    int[] arr = new int[50];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = random.nextInt(35);
    }

    var myRecursiveAction = new CustomRecursiveTask(arr);
    var forkJoinPool = PoolUtil.forkJoinPool;
    forkJoinPool.execute(myRecursiveAction);
    myRecursiveAction.join();
    System.out.println(myRecursiveAction.isDone());
    System.out.println(myRecursiveAction.get());

    var myRecursiveAction2 = new CustomRecursiveTask(arr);
    var forkJoinPool2 = ForkJoinPool.commonPool();
    forkJoinPool2.submit(myRecursiveAction2);
    myRecursiveAction2.join();
    System.out.println(myRecursiveAction2.isDone());
    System.out.println(myRecursiveAction2.get());

    var myRecursiveAction3 = new CustomRecursiveTask(arr);
    ForkJoinPool.commonPool().invoke(myRecursiveAction3);
    System.out.println(myRecursiveAction3.isDone());
    System.out.println(myRecursiveAction3.get());

    CustomRecursiveTask customRecursiveTaskFirst = new CustomRecursiveTask(arr);
    CustomRecursiveTask customRecursiveTaskSecond = new CustomRecursiveTask(arr);

    customRecursiveTaskFirst.fork();
    customRecursiveTaskSecond.fork();
    int result = 0;
    result += customRecursiveTaskSecond.join();
    result += customRecursiveTaskFirst.join();
    System.out.println(customRecursiveTaskFirst.isDone());
    System.out.println(customRecursiveTaskSecond.isDone());
    System.out.println(result);
  }
}
