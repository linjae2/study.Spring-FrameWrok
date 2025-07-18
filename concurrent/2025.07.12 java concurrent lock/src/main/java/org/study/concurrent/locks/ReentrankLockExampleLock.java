package org.study.concurrent.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrankLockExampleLock {

  private static ReentrantLock getReentrantLock() {
    ReentrantLock lock = new ReentrantLock();
    return lock;
  }

  public static void main(String args[]) throws InterruptedException {
    System.out.println("================= START ======================");
    ReentrantLock lock = getReentrantLock();

    Thread thread1 = new Thread(() -> {
      lock.tryLock();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("Thread was interrupted during sleep.");
      } finally {
        lock.unlock();
      }
      System.out.println("스레드 종료.");
    }, "thread_01");

    Thread thread2 = new Thread(() -> {
      lock.lock();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("Thread was interrupted during sleep.");
      } finally {
        lock.unlock();
      }
      System.out.println("스레드 종료.");
    }, "thread_02");

    thread1.start();
    thread2.start();
    Thread.sleep(50);

    lock.lock();
    try {

    } finally {
      lock.unlock();
    }
  }
}
