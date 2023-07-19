package org.example.threadlocal;

public class ThreadLocalWithUserContext implements Runnable {

  private static final ThreadLocal<Context> userContext = new ThreadLocal<>();
  private final Integer userId;
  private final UserRepository userRepository = new UserRepository();

  ThreadLocalWithUserContext(Integer userId) {
    this.userId = userId;
  }

  @Override
  public void run() {
    String userName = userRepository.getUserNameForUserId(userId);
    userContext.set(new Context(userName));
    System.out.println("thread context for given userId: " + userId + " is: " + userContext.get());
  }

  public static void main(String[] args) throws InterruptedException {
    ThreadLocalWithUserContext firstUser = new ThreadLocalWithUserContext(1);
    ThreadLocalWithUserContext secondUser = new ThreadLocalWithUserContext(2);
    new Thread(firstUser).start();
    new Thread(secondUser).start();

    Thread.sleep(3000);
  }
}
