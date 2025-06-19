package org.example.tests.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.tests.order.Order;
import org.example.tests.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class OrderTest extends JpaTestContext {

  @Test
  @DisplayName("persist를 수행하면 ID 가 할당된다.")
  @Rollback(value = false)
  void persist() {
    // ginven
    Order order = Order.builder().build();

    // when
    em.persist(order);

    // then
    assertThat(order.getId()).isNotNull();
  }
}
