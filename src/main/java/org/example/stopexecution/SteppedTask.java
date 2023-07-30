package org.example.stopexecution;

import java.util.List;
import java.util.Timer;
import java.util.stream.Stream;

public class SteppedTask implements Runnable {

  private final List<Step> steps;

  public SteppedTask(List<Step> steps) {
    this.steps = steps;
  }

  @Override
  public void run() {
    System.out.println("running stepped process");
    for (Step step : steps) {
      System.out.println("running step " + step.number);
      try {
        step.perform();
      } catch (InterruptedException e) {
        System.out.println("interrupting task");
        break;
      }
    }
    System.out.println("stepped process finished");
  }

  public static void main(String[] args) {
    List<Step> steps = Stream.of(
            new Step(1),
            new Step(2),
            new Step(3),
            new Step(4))
        .toList();

    Thread thread = new Thread(new SteppedTask(steps));
    thread.start();

    Timer timer = new Timer();
    TimeOutTask timeOutTask = new TimeOutTask(thread, timer);
    timer.schedule(timeOutTask, 10000);
  }
}
