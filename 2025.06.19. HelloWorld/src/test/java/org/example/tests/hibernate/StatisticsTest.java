package org.example.tests.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.example.tests.product.Comment;
import org.example.tests.product.Product;
import org.example.tests.product.ProductRepository;
import org.example.tests.support.QueryCountUtil;
import org.example.tests.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class StatisticsTest extends JpaTestContext {
  @Autowired
  ProductRepository productRepository;

  @Test
  @DisplayName("findAll로 Product를 조회해도 loadCount는 0이다.")
  @Rollback(value = false)
  void findAll_loadCountIsZero() {
    // ginven
    saveProductWithThreeComment();

    // when
    List<Product> products = productRepository.findAll();

    System.out.println(products);
    // then
    long loadCount = QueryCountUtil.getEntityLoadCount(em);

    // 지연 로딩으로 Entity를 조회하지 않는다.
    assertEquals(0, loadCount);
  }

  private void saveProductWithThreeComment() {
    Product product = new Product();
    Comment comment1 = new Comment();
    comment1.setProduct(product);
    product.setComments(List.of(
      // comment1,
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
