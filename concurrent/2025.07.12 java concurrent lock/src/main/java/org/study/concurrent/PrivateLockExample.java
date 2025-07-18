package org.study.concurrent;

class Counter {
  private int count = 0;

  public synchronized void increment() {
    count++;
  }

  public int getValue() {
    return count;
  }
}

class Worker implements Runnable {
  private Counter counter;

  public Worker(Counter counter) {
    this.counter = counter;
  }

  public void run() {
    for (int i = 0; i < 10000; i++) {
      counter.increment();
    }
  }
}

public class PrivateLockExample {
  public static void main(String[] args) throws InterruptedException {
    Counter counter = new Counter();
    Thread worker = new Thread(new Worker(counter));

    worker.start();
    // 내부에서 무한정 대기하므로 객체 counter의 락이 풀리지 않음
    synchronized (counter) {
      while (true) {
        Thread.sleep(Integer.MAX_VALUE);
      }
    }
  }
}
