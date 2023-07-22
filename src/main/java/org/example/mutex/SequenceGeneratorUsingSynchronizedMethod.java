package org.example.mutex;

import static org.example.mutex.Test.getUniqueSequences;

public class SequenceGeneratorUsingSynchronizedMethod extends SequenceGenerator {

  @Override
  public synchronized int getNextSequence() {
    return super.getNextSequence();
  }

  public static void main(String[] args) throws Exception {
    getUniqueSequences(new SequenceGeneratorUsingSynchronizedMethod(), 1000);
  }

}
