package org.study.concurrent;

import org.study.concurrent.WaitingStateExample.BlockedTask;
import org.study.concurrent.WaitingStateExample.TimedWaitingTask;
import org.study.concurrent.WaitingStateExample.WaitingRunnalbe;
import org.study.concurrent.WaitingStateExample.WorkingRunnalbe;

public class WaitingStateExample {
  private static final Object lock = new Object();

  public static void main(String args[]) throws InterruptedException {
    Thread workingThread = new Thread(new WorkingRunnalbe());
    Thread waitingThread = new Thread(new WaitingRunnalbe(workingThread));
    Thread timedWaitingThread = new Thread(new TimedWaitingTask());

    Thread blockedThread = new Thread(new BlockedTask(), "스레드 1");

    synchronized (lock) {
      workingThread.start();
      waitingThread.start();
      timedWaitingThread.start();
      blockedThread.start();

      Thread.sleep(500); // 충분한 시간동안 스레드들이 실행될 수 있게 기다림
      System.out.println("WorkingThread 현재 상태: " + workingThread.getState()); // TIMED_WAITING
      System.out.println("WaitingThread 현재 상태: " + waitingThread.getState()); // WAITING
      System.out.println("TimedWaitingThread 현재 상태: " + timedWaitingThread.getState()); // WAITING

      System.out.println("BlockedThread 현재 상태: " + blockedThread.getState()); // WAITING
    }
    workingThread.join();
    waitingThread.join();
    timedWaitingThread.join();
  }

  public static class WorkingRunnalbe implements Runnable{
    @Override
    public void run() {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static class WaitingRunnalbe implements Runnable {
    private final Thread workingThread;

    public WaitingRunnalbe(Thread workingThread) {
      this.workingThread = workingThread;
    }

    @Override
    public void run() {
      try {
        workingThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static class TimedWaitingTask implements Runnable {
    @Override
    public void run() {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static class BlockedTask implements Runnable {
    @Override
    public void run() {
      synchronized (lock) {
        System.out.println(Thread.currentThread().getName() + "이(가) 락을 얻었습니다.");
      }
    }
  }
}
