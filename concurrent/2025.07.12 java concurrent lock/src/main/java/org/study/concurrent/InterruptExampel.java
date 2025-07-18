package org.study.concurrent;

public class InterruptExampel {
  public static void main(String[] args) {
    Thread mainThread = Thread.currentThread();

    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(5);
        // mainThread.interrupt();;
        while(!Thread.currentThread().isInterrupted()) {
          System.out.println("Running ...");
          // Thread.sleep(500);
        }
      } catch (InterruptedException e) {
        System.out.println("Thread was interrupted during sleep.");
      }


      if (Thread.interrupted()) { //.currentThread().isInterrupted()) {
        System.out.println("> Thread isInterrupted....");
      }

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        System.out.println("Thread was interrupted during sleep.");
      }

      if (Thread.currentThread().isInterrupted()) {
        System.out.println("> Thread isInterrupted....");
      }
      System.out.println("Thread exiting....");
    });

    thread.start();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println("========================");
      Thread.currentThread().interrupt();
    }

    thread.interrupt();
  }
}
