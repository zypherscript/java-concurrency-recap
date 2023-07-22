package org.example.mutex;

import static org.example.mutex.Test.getUniqueSequences;

import java.util.concurrent.locks.ReentrantLock;

public class SequenceGeneratorUsingReentrantLock extends SequenceGenerator {

  private final ReentrantLock mutex = new ReentrantLock();

  @Override
  public int getNextSequence() {
    try {
      mutex.lock();
      return super.getNextSequence();
    } finally {
      mutex.unlock();
    }
  }

  public static void main(String[] args) throws Exception {
    getUniqueSequences(new SequenceGeneratorUsingReentrantLock(), 1000);
  }
}
