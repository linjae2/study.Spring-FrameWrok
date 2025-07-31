package com.violetbeach.hibernateplayground.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.violetbeach.hibernateplayground.order.Order;
import com.violetbeach.hibernateplayground.product.Comment;
import com.violetbeach.hibernateplayground.product.Product;
import com.violetbeach.hibernateplayground.product.ProductRepository;
import com.violetbeach.hibernateplayground.support.QueryCountUtil;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StatisticsTest extends JpaTestContext {
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("findAll로 Product를 조회해도 loadCount는 0이다.")
    void findAll_loadCountIsZero() {
        // given
        saveProductWithThreeComment();

        // when
        productRepository.findAll();

        // then
        long loadCount = QueryCountUtil.getEntityLoadCount(em);
        // 지연 로딩으로 Entity를 조회하지 않는다.
        assertEquals(0, loadCount);
    }

    private void saveProductWithThreeComment() {
        Product product = new Product();
        product.setComments(List.of(
            new Comment(),
            new Comment(),
            new Comment(),
            new Comment()
        ));
        productRepository.save(product);
        em.flush();
        em.clear();
    }

}
