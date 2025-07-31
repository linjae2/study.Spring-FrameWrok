package com.violetbeach.hibernateplayground.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest extends JpaTestContext {

    @Test
    @DisplayName("persist를 수행하면 ID가 할당된다.")
    void persist() {
        // given
        Order order = Order.builder()
            .build();

        // when
        em.persist(order);

        // then
        assertThat(order.getId()).isNotNull();
    }

}
