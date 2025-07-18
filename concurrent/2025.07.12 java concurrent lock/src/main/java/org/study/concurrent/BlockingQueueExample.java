package org.study.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueExample {

  public static void main(String[] args) {
    BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);

    // 프로듀서 스레드
    Thread producer = new Thread(() -> {
      int value = 0;
      while (true) {
        try {
          queue.put(value);
          System.out.println("Produced " + value);
          value++;
          Thread.sleep(10);
        } catch (InterruptedException e) {

        }
      }
    });

    // 컨슈머 스레드
    Thread consumer = new Thread(() -> {
      try {
        int value = queue.take();
        System.out.println("Consumed " + value);
        Thread.sleep(150);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    });

    producer.start();
    consumer.start();
  }
}
