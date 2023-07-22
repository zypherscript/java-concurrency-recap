package org.example.async;

public class JavaAsync {

  public static long factorial(int number) {
    long result = 1;
    for (int i = number; i > 0; i--) {
      result *= i;
    }
    return result;
  }
  
}
