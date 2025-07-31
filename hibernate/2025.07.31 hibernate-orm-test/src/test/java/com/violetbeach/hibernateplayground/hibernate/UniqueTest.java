package com.violetbeach.hibernateplayground.hibernate;

import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import com.violetbeach.hibernateplayground.unique.Book;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UniqueTest extends JpaTestContext {

    /**
     * 혹시 앱에서 처리 가능한 예외가 터질 것을 기대했으나,
     * 불가능 - DDL 생성 시에만 적용되며 런타임에는 효력이 없다.
     */
    @Test
    @DisplayName("Unique 필드를 지정해도 ConstraintViolationException가 발생한다.")
    void GeneratedValueAnnotation_supplyCompositeKey() {
        // given
        String duplicatedTitle = "Little Prince";

        // when & then
        assertThrows(ConstraintViolationException.class, () -> {
            for (int i = 0; i < 2; i++) {
                em.persist(new Book(duplicatedTitle));
            }
        });
    }
}
