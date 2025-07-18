package org.study.concurrent;

public class ThreadJoinExample_01 {
  public static void main(String args[]) {
    // 앞에서 만든 스레드 B를 만든 후 start
    // 해당 스레드가 실행되면, 해당 스레드는 run메소드 안에서
    // 자신의 모니터링 락을 획득
    Thread_B b = new Thread_B();
    b.start();

    // // Exception in thread "main" java.lang.IllegalMonitorStateException: current thread is not owner
    // try {
    //   b.wait();
    // } catch (InterruptedException e) {
    //   // TODO Auto-generated catch block
    //   e.printStackTrace();
    // }

    // b에 대하여 동기화 블럭을 설정
    // 만약 main스레드가 아래의 블록을 위의
    // Thread보다 먼저 실행되었다면 wait를 하게 되면서
    synchronized(b) {
      try {
        System.out.println("b가 완료될때까지 기다립니다.");
        b.wait();
        System.out.println("===================");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    //깨어난 후 결과를 출력
    System.out.println("Total is: " + b.total);
  }
}

class Thread_B extends Thread {
  int total;
  @Override
  public void run() {
    synchronized(this) {
      for (int i = 0; i < 5; i++) {
        System.out.println(i + "를 더합니다.");
        total += i;
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      notify();
      System.out.println("덧셈을 완료하였습니다.");
      // notifyAll();

      for (int i = 0; i < 5; i++) {
        System.out.println(i + "를 더합니다.");
        total += i;
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}