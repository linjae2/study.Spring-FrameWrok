package org.study.concurrent;

class WorkObject{
  public synchronized void methodA() {
    System.out.println("ThreadA의 methodA() 작업 실행");
    notify(); // 일시 정지 상태에 있는 ThreadA를 실행 대기 상태로 전환
		try{
			wait(); // ThreadB를 일시 정지 상태로 전환
		}
		catch(InterruptedException e){}
  }

  public synchronized void methodB() {
    System.out.println("ThreadB의 methodB() 작업 실행");
    notify(); // 일시 정지 상태에 있는 ThreadA를 실행 대기 상태로 전환
		try{
			wait(); // ThreadB를 일시 정지 상태로 전환
		}
		catch(InterruptedException e){}
  }
}

class ThreadA extends Thread {
  private WorkObject workObject;

  public ThreadA(WorkObject workObject) {
    this.workObject = workObject;
  }

  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      workObject.methodA();
    }
  }
}

class ThreadB extends Thread {
  private WorkObject workObject;

  public ThreadB(WorkObject workObject) {
    this.workObject = workObject;
  }

  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      workObject.methodB();
    }
  }
}

public class ThreadTest {

  public static void main(String args[]) {
    WorkObject sharedObject = new WorkObject();

    ThreadA threadA = new ThreadA(sharedObject);
    ThreadB threadB = new ThreadB(sharedObject);

    threadA.start();
    threadB.start();
  }
}
