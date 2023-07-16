package org.example.utilconcurrent;

import java.util.concurrent.Executor;

public class Invoker implements Executor {

  @Override
  public void execute(Runnable r) {
    r.run();
  }
}