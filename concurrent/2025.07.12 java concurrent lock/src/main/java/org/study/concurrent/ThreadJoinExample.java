package org.study.concurrent;

import java.time.LocalDateTime;

public class ThreadJoinExample {
  public static void main(String args[]) throws InterruptedException {
    // 3초 쉬고 끝나는 스레드
    Thread thread1 = new Thread(() -> {
        System.out.println("Thread Test: " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    });

    Thread thread2 = new Thread(() -> {
        System.out.println("Thread Test: " + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    });

    thread1.start(); thread2.start();
    System.out.println("Main 실행 시간: " + LocalDateTime.now()); // main 스레드

    thread1.join(); // main 스레드가 thread3이 끝날때 까지 기다림(3초)
    System.out.println("Main 종료 시간: " + LocalDateTime.now()); // 만약 위의 join으로 기다리지 않는 다면 이 출력문은 아무때나 출력이 됩니다.

    thread2.join(); // main 스레드가 thread3이 끝날때 까지 기다림(3초)
    System.out.println("Main 종료 시간: " + LocalDateTime.now()); // 만약 위의 join으로 기다리지 않는 다면 이 출력문은 아무때나 출력이 됩니다.
  }
}
