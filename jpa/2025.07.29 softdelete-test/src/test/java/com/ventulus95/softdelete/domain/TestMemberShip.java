package com.ventulus95.softdelete.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TestMemberShip {

    @PersistenceContext
    EntityManager em;

    @Test
    void 별도컬럼없이도_자동으로_소프트딜리트컬럼이_생기는지() {
        MemberShip memberShip = new MemberShip("하이요");
        em.persist(memberShip);
        assertThat(memberShip.getTestNo() , is(1L));
    }


    @Test
    void 소프트딜리트컬럼이_기본값은_false인가() {
        MemberShip memberShip = new MemberShip("하이요");
        em.persist(memberShip);
        System.out.println(memberShip.isDelete());
        assertFalse(memberShip.isDelete());
    }

    @Test
    void 소프트딜리트가_정상동작하는지() {
        MemberShip memberShip = new MemberShip("하이요");
        em.persist(memberShip);
        em.remove(memberShip);
        em.flush();

        //이건 객체단위에서는 체크가 불가능
        assertFalse(memberShip.isDelete());
    }


    @Test
    void 소프트딜리트시_조회가_안되는지() {
        MemberShip memberShip = new MemberShip("하이요");
        em.persist(memberShip);
        em.remove(memberShip);
        em.flush();

        MemberShip findMembership = em.find(MemberShip.class, 1L);
        assertThat(findMembership, nullValue());
    }




}