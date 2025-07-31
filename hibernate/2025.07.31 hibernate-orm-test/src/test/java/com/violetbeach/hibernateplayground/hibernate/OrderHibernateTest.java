package com.violetbeach.hibernateplayground.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.violetbeach.hibernateplayground.order.Order;
import com.violetbeach.hibernateplayground.support.QueryCountUtil;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderHibernateTest extends JpaTestContext {

    /**
     * Lazy 옵션을 사용하려면 ByteCode 향상 플러그인과 enableLazyInitialization 속성 명시
     */
    @Test
    @DisplayName("@Basic의 fetch가 LAZY여도 기본적으로 해당 필드를 EAGER로 가져온다.")
    void lazyField_ItWorksEager() {
        // given
        Order order = Order.builder()
            .title("Title")
            .build();

        em.persist(order);
        em.flush();
        em.clear();

        // when
        Order findOrder = em.find(Order.class, order.getId());
        String title = findOrder.getTitle();

        // then
        long selectQueryCount = QueryCountUtil.getEntityLoadCount(em);
        assertEquals(1, selectQueryCount);
    }

}
