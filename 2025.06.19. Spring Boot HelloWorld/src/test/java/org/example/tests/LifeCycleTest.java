package org.example.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LifeCycleTest {
  public LifeCycleTest() {
    System.out.println("new LifeCycleTest()");
  }

  @BeforeEach
  void setUp() {
    System.out.println("setUp()");
  }

  @AfterEach
  void tearDown() {
    System.out.println("tearDown()");
  }

  @Test
  void a() {
    System.out.println("a()");
  }

  @Test
  void b() {
    System.out.println("b()");
  }
}
