package org.study.concurrent.locks;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

class ReentrantLockWithCondition {
  Stack<String> stack = new Stack<>();
  int CAPACITY = 2;

  ReentrantLock lock = new ReentrantLock();
  Condition stackEmptyCondition = null;
  Condition stackFullCondition = null;

  public ReentrantLockWithCondition() {
    stackEmptyCondition = lock.newCondition();
    stackFullCondition = lock.newCondition();
  }

  public void pushToStack(String item) throws InterruptedException {
    lock.lock();
    try {
      while(stack.size() == CAPACITY) {
        stackFullCondition.await();
      }

      stack.push(item);
      stackEmptyCondition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public String popFromStack() throws InterruptedException {
    lock.lock();
    try {
      while(stack.size() == 0) {
        stackEmptyCondition.await();
      }
      String rs = stack.pop();
      stackFullCondition.signalAll();
      return rs;
    } finally {
      lock.unlock();
    }
  }
}

public class ReentrankLockExample {

  private static ReentrantLockWithCondition wc = new ReentrantLockWithCondition();
  final static ExecutorService executor = Executors.newFixedThreadPool(2);

  private static ExecutorService getExecutor() {
    return executor;
  }

  public static void main(String args[]) throws InterruptedException {
    System.out.println("================= START ======================");
    ExecutorService executor = getExecutor();

    IntStream.range(0, 10).forEach(n ->
      executor.execute( () -> {
        try {
          System.out.println(" ========= " + wc.popFromStack());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      })
    );

    // executor.execute( () -> {
    //   try {
    //     System.out.println(" ========= pushToStack()");
    //     wc.pushToStack(Thread.currentThread().getName() + "_main");
    //   } catch (InterruptedException e) {
    //     e.printStackTrace();
    //   }
    // });

    Thread.sleep(10000);


    // IntStream.range(0, 10).forEach(n ->
    //   executor.execute( () -> {
    //     try {
    //       wc.pushToStack(Thread.currentThread().getName() + "_aaaaaa");
    //     } catch (InterruptedException e) {
    //       e.printStackTrace();
    //     }
    //   })
    // );

    // Thread thread0 = new Thread(() -> {
    //   try {
    //     wc.stackFullCondition.await();
    //   } catch (InterruptedException e) {
    //     System.out.println("Thread was interrupted during sleep.");
    //   }
    // });
    // thread0.start();
    // Thread.sleep(1000000);


    wc.pushToStack(Thread.currentThread().getName() + "_main");
    wc.pushToStack(Thread.currentThread().getName() + "_main");
    Thread thread1 = new Thread(() -> {
      try {
        wc.pushToStack(Thread.currentThread().getName() + "_aaaaaa");
      } catch (InterruptedException e) {
        System.out.println("Thread was interrupted during sleep.");
      }
    });

    Thread thread2 = new Thread(() -> {
      try {
        wc.pushToStack(Thread.currentThread().getName() + "_aaaaaa");
      } catch (InterruptedException e) {
        System.out.println("Thread was interrupted during sleep.");
      }
    });
    thread1.start();
    thread2.start();


    Thread.sleep(10);
    wc.pushToStack(Thread.currentThread().getName() + "_main");
    // System.out.println(wc.popFromStack());
    // System.out.println(wc.popFromStack());
    // System.out.println(wc.popFromStack());
    // System.out.println(wc.popFromStack());
    // System.out.println(wc.popFromStack());
    // System.out.println(wc.popFromStack());
    // System.out.println(wc.popFromStack());
  }
}
