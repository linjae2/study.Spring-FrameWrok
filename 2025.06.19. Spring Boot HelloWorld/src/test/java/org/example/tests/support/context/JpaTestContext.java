package org.example.tests.support.context;

import org.example.tests.support.QueryCountUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTestContext {
  @PersistenceContext
  protected EntityManager em;

  @BeforeEach
  void setup() {
    QueryCountUtil.clearAllCount(em);
  }
}
