package org.example.runnablecallable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FactorialTask implements Callable<Integer> {

  private final int number;

  public FactorialTask(int number) {
    this.number = number;
  }

  public Integer call() throws InvalidParamaterException {
    if (number < 0) {
      throw new InvalidParamaterException("Number should be positive");
    }

    int fact = 1;
    for (int count = number; count > 1; count--) {
      fact = fact * count;
    }

    return fact;
  }

  public static void main(String[] args) {
    var executorService = Executors.newCachedThreadPool();
    FactorialTask task = new FactorialTask(-1);
    Future<Integer> future = executorService.submit(task);
    System.out.println(future.isDone());

    Integer value = null;
    try {
      value = future.get();
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
    }
    System.out.println(value);

    executorService.shutdown();
  }

  private static class InvalidParamaterException extends Exception {

    public InvalidParamaterException(String message) {
      super(message);
    }
  }
}
