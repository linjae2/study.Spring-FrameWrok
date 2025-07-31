package com.violetbeach.hibernateplayground.hibernate;

import com.violetbeach.hibernateplayground.compositepk.PK;
import com.violetbeach.hibernateplayground.compositepk.SystemUser;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompositePKTest extends JpaTestContext {

    /**
     * IDENTIFY 방식은 지원하지 않는다.
     */
    @Test
    @DisplayName("@GeneratedValue는 복합 키도 지원한다.")
    void GeneratedValueAnnotation_supplyCompositeKey() {
        // given
        SystemUser systemUser = new SystemUser("KakaoPay", "VioletBeach", "Lee");

        // when
        em.persist(systemUser);

        // then
        PK pk = systemUser.getId();
        assertThat(pk.getRegistrationId()).isNotNull();
    }
}
