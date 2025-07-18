package org.study.concurrent.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ReentrankLockExampleContition {

  // private static ReentrantLock getReentrantLock() {
  //   return SingletonHelper.INSTANCE;
  // }

  // private static Condition getCondition() {
  //   return SingletonHelper01.INSTANCE;
  // }

  // private static class SingletonHelper {
  //   private static final ReentrantLock INSTANCE = new ReentrantLock();
  // }

  // private static class SingletonHelper01 {
  //   private static final Condition INSTANCE = getReentrantLock().newCondition();
  // }

  private static final ReentrantLock lock = new ReentrantLock();
  private static final Condition cond = lock.newCondition();

  public static void main(String args[]) throws InterruptedException {
    System.out.println("================= START ======================");
    Thread a = new Thread(getRunnable01("A "), "thA");
    Thread b = new Thread(getRunnable01("B "), "thB");
    Thread c = new Thread(getRunnable01("C "), "thC");
    Thread d = new Thread(getRunnable01("D "), "thD");
    Thread e = new Thread(getRunnable02("E "), "thE");

    a.start();
    b.start();
    c.start();
    d.start();
    // e.start();
    Thread.sleep(50);

    lock.lock();
    try {
      System.out.println("======== =========");
      d.interrupt();
      cond.signal();
      // cond.signalAll();
    } finally {
      lock.unlock();
    }
  }

  static Runnable getRunnable01(String threadName) {
    return () -> {
      System.out.println("====== " + threadName + " =========");
      try {
        lock.lockInterruptibly();
        try {
          System.out.println("==> " + threadName);
          Thread.sleep(10);
          cond.await();
          System.out.println(threadName);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
  }

  static Runnable getRunnable02(String threadName) {
    return () -> {
      lock.lock();
      try {
        System.out.println("==> " + threadName);
        Thread.sleep(1000);
        System.out.println(threadName);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    };
  }
}
