package org.study.concurrent;

public class ThreadScheduling {
  public static void main(String[] args) {
    ThreadScheduling_1 th1 = new ThreadScheduling_1();
    ThreadScheduling_2 th2 = new ThreadScheduling_2();


    th1.start();
    th2.start();

    th1.getState();
    try {
      th1.sleep(2000);
    } catch (InterruptedException e) {
    }
    System.out.print("<<main 종료>>");
  }
}

class ThreadScheduling_1 extends Thread{
  @Override
  public void run() {
    for (int i=0;i<300;i++) {
        System.out.print("-");
    }
    System.out.print("<<th1 종료>>");
  }
}
class ThreadScheduling_2 extends Thread{
  @Override
  public void run() {
    for (int i=0;i<300;i++) {
        System.out.print("|");
    }
    System.out.print("<<th2 종료>>");
  }
}