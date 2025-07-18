package org.study.concurrent;

public class InterruptExample_01 {

  public static void main(String args[]) {
    Thread thread = new PrintThread();
    thread.start();

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // 스레드를 종료시키기 위해 InterruptedException 발생
    thread.interrupt();
    try {
      thread.join();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}

class PrintThread extends Thread {
  @Override
  public void run() {
    try {
      while(true) {
        System.out.println("실행 중");
        Thread.sleep(1);
      }
    } catch (InterruptedException e) {
      System.out.println("interrupted 발생~");
      e.printStackTrace();
    }
    System.out.println("자원 정리");
    System.out.println("실행 종료");
  }
}