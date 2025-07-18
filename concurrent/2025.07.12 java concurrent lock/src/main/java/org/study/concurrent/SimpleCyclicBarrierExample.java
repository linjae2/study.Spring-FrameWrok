package org.study.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleCyclicBarrierExample {

  public static void main(String[] args) throws InterruptedException {
    int numOfThreads = 4;
    CyclicBarrier barrier = new CyclicBarrier(numOfThreads, new BarrierAction());

    for (int i = 0; i < numOfThreads; i++) {
      new Thread(new Worker(barrier)).start();
    }

    Thread.sleep(5000);
  }

  public static class BarrierAction implements Runnable {
    @Override
    public void run() {
      System.out.println("모든 스레드가 작업을 완료했습니다!");
    }
  }

  static class Worker implements Runnable {
    private final CyclicBarrier barrier;

    public Worker(CyclicBarrier barrier) {
      this.barrier = barrier;
    }

    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName() + ": 안녕하세요!");

      // 시간이 오래 걸리는 작업을 수행하는 코드
      try {
        // 시뮬레이션을 위해 무작위로 스레드를 잠시 중지한다.
        int randomDelay = ThreadLocalRandom.current().nextInt(1, 6) * 100;
        Thread.sleep(randomDelay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      System.out.println(Thread.currentThread().getName() + "이(가) 작업을 완료했습니다.");

      try {
        barrier.await();
        System.out.println(Thread.currentThread().getName() + "이(가) 배리어를 통과했습니다.");
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
  }
}
