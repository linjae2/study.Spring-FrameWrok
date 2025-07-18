package org.study.concurrent;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
  public static void main(String[] args) throws InterruptedException {
    int numOfTasks = 4;
    CountDownLatch latch = new CountDownLatch(numOfTasks);

    for (int i = 1; i <= numOfTasks; i++) {
        Task task = new Task(i, latch);
        new Thread(task).start();
    }

    latch.await();
    System.out.println("모든 작업이 완료되었습니다!");
  }
}

class Task implements Runnable {
  private final int taskId;
  private final CountDownLatch latch;

  public Task(int taskId, CountDownLatch latch) {
    this.taskId = taskId;
    this.latch = latch;
  }

  @Override
  public void run() {
    System.out.println("작업 #" + taskId + "이 실행 중입니다...");
    // 시간이 오래 걸리는 작업을 수행하는 코드
    try {
        // 시뮬레이션을 위해 무작위로 스레드를 잠시 중지한다.
        Thread.sleep((int) (Math.random() * 3000));
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("작업 #" + taskId + "이 완료되었습니다!");
    latch.countDown();
  }
}