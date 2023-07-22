package org.example.mutex;

import static org.example.mutex.Test.getUniqueSequences;

import java.util.concurrent.Semaphore;

public class SequenceGeneratorUsingSemaphore extends SequenceGenerator {

  private final Semaphore mutex = new Semaphore(1);

  @Override
  public int getNextSequence() {
    try {
      mutex.acquire();
      return super.getNextSequence();
    } catch (InterruptedException e) {
      throw new RuntimeException("Exception in critical section.", e);
    } finally {
      mutex.release();
    }
  }

  public static void main(String[] args) throws Exception {
    getUniqueSequences(new SequenceGeneratorUsingSemaphore(), 1000);
  }
}
