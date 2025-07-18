package org.study.concurrent.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class ReentrankLockExample_01 {

  static Integer count = 1;

  private static ReentrantLock getReentrantLock() {
    return SingletonHelper.INSTANCE;
  }

  private static class SingletonHelper {
    private static final ReentrantLock INSTANCE = new ReentrantLock();
  }

  private static class SingletonHelper01 {
    private static final ReentrantLock INSTANCE = new ReentrantLock();
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("================= START ======================");

    Thread a = new Thread(addCount("A "));
    Thread b = new Thread(addCount("B "));
    Thread c = new Thread(addCount("C "));
    Thread d = new Thread(addCount("D "));

    a.start();
    b.start();
    c.start();
    d.start();

    a.join();
    b.join();
    c.join();
    d.join();
  }

  static Runnable addCount(String threadName){
    return () -> IntStream.range(0, 100)
      .forEach(i -> {
        ReentrantLock lock = getReentrantLock();
        try {
          if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
            try {
              System.out.println(threadName + count++);
            } finally {
              lock.unlock();
            }
          }
          else {
            System.out.println(threadName + "failed to acquire, count = " + count);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
  }

  static Runnable addCount4(String threadName){
    return () -> IntStream.range(0, 100)
      .forEach(i -> {
        ReentrantLock lock = getReentrantLock();
        if (lock.tryLock()) {
          try {
            System.out.println(threadName + count++);
          } finally {
            lock.unlock();
          }
        }
        else {
          System.out.println(threadName + "failed to acquire, count = " + count);
        }
      });
  }

  static Runnable addCount3(String threadName){
    return () -> IntStream.range(0, 100)
      .forEach(i -> {
        ReentrantLock lock = getReentrantLock();
        lock.lock();
        try {
          System.out.println(threadName + count++);
        } finally {
          lock.unlock();
        }
      });
  }

  static Runnable addCount2(String threadName){
    return () -> IntStream.range(0, 100)
      .forEach(i -> {
        synchronized(ReentrankLockExample_01.class) {
          System.out.println(threadName + count++);
        }
      });
  }

  static Runnable addCount1(String threadName){
    return () -> IntStream.range(0, 100)
      .forEach(i -> {
          System.out.println(threadName + count++);
      });
  }
}
