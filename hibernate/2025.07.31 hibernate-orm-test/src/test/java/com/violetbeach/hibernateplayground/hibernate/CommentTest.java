package com.violetbeach.hibernateplayground.hibernate;

import com.violetbeach.hibernateplayground.mask.Mask;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest extends JpaTestContext {

    @Test
    @DisplayName("@Comment는 생성하는 DDL에서 Comment를 작성한다.")
    void Comment_ItWriteDDL() {
        // when
        Mask mask = new Mask();

        em.persist(mask);
        em.flush();
        em.clear();
    }

}
