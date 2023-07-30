package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class ThreadSafetyTest {

  @Test
  void syncCollectionTest() throws InterruptedException {
    Collection<Integer> syncCollection = Collections.synchronizedCollection(new ArrayList<>());
    Thread thread1 = new Thread(() -> syncCollection.addAll(Arrays.asList(1, 2, 3, 4, 5, 6)));
    Thread thread2 = new Thread(() -> syncCollection.addAll(Arrays.asList(7, 8, 9, 10, 11, 12)));
    thread1.start();
    thread2.start();

    Thread.sleep(1000);
    System.out.println(syncCollection);
  }

}
