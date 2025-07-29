package com.ventulus95.softdelete.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberDateTimeTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void 삭제시간이_시간으로_들어가는지() {
        MemberDateTime memberDateTime = new MemberDateTime("안녕하세요");
        em.persist(memberDateTime);
        em.remove(memberDateTime);
        em.flush();
        System.out.println(memberDateTime.getTestNo());
        MemberDateTime find = em.find(MemberDateTime.class, memberDateTime.getTestNo());
        System.out.println(find.getTestNo());

        assertThat(memberDateTime, is(find));

    }
}