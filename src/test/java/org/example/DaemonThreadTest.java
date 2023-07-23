package org.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.daemon.NewThread;
import org.junit.jupiter.api.Test;

public class DaemonThreadTest {


  @Test
  public void whenSetDaemonWhileRunning_thenIllegalThreadStateException() {
    assertThrows(IllegalThreadStateException.class, () -> {
      NewThread daemonThread = new NewThread();
      daemonThread.start();
      daemonThread.setDaemon(true);
    });
  }

  @Test
  public void whenCallIsDaemon_thenCorrect() {
    NewThread daemonThread = new NewThread();
    NewThread userThread = new NewThread();
    daemonThread.setDaemon(true);
    daemonThread.start();
    userThread.start();

    assertTrue(daemonThread.isDaemon());
    assertFalse(userThread.isDaemon());
  }
}
