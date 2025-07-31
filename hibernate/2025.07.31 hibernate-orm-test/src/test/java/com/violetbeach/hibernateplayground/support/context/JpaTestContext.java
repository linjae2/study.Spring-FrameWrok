package com.violetbeach.hibernateplayground.support.context;

import com.violetbeach.hibernateplayground.support.QueryCountUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class JpaTestContext {
    @PersistenceContext
    protected EntityManager em;

    @BeforeEach
    void setup() {
        QueryCountUtil.clearAllCount(em);
    }
}
