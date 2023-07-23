package org.example.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import org.example.forkjoin.util.PoolUtil;

public class CustomRecursiveAction extends RecursiveAction {

  private String workload = "";
  private static final int THRESHOLD = 4;

  public CustomRecursiveAction(String workload) {
    this.workload = workload;
  }

  @Override
  protected void compute() {
    if (workload.length() > THRESHOLD) {
      ForkJoinTask.invokeAll(createSubtasks());
    } else {
      processing(workload);
    }
  }

  private List<CustomRecursiveAction> createSubtasks() {
    List<CustomRecursiveAction> subtasks = new ArrayList<>();

    String partOne = workload.substring(0, workload.length() / 2);
    String partTwo = workload.substring(workload.length() / 2);

    subtasks.add(new CustomRecursiveAction(partOne));
    subtasks.add(new CustomRecursiveAction(partTwo));

    return subtasks;
  }

  private void processing(String work) {
    String result = work.toUpperCase();
    System.out.println("This result - (" + result + ") - was processed by "
        + Thread.currentThread().getName());
  }

  public static void main(String[] args) {
    CustomRecursiveAction myRecursiveAction = new CustomRecursiveAction("ddddffffgggghhhh");
    var forkJoinPool = PoolUtil.forkJoinPool;
    forkJoinPool.execute(myRecursiveAction);
    myRecursiveAction.join();
    System.out.println(myRecursiveAction.isDone());

//    Use as few thread pools as possible. In most cases, the best decision is to use one thread pool per application or system.
//    Use the default common thread pool if no specific tuning is needed.
    CustomRecursiveAction myRecursiveAction2 = new CustomRecursiveAction("ddddffffgggghhhh");
    var forkJoinPool2 = ForkJoinPool.commonPool();
    forkJoinPool2.submit(myRecursiveAction2);
    myRecursiveAction2.join();
    System.out.println(myRecursiveAction2.isDone());

    CustomRecursiveAction myRecursiveAction3 = new CustomRecursiveAction("ddddffffgggghhhh");
    ForkJoinPool.commonPool().invoke(myRecursiveAction3);
    System.out.println(myRecursiveAction3.isDone());
  }
}
