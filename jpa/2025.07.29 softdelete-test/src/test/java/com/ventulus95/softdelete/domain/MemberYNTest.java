package com.ventulus95.softdelete.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberYNTest {

    @PersistenceContext
    EntityManager em;


    @Test
    void yn컬럼이_정상적으로_작동하는가() {
        MemberYN memberYN = new MemberYN("test");
        em.persist(memberYN);
        em.clear();

        MemberYN find = em.find(MemberYN.class, memberYN.getTestNo());
        assertThat(find.getDelYn(), is("N"));
    }

    @Test
    void yn컬럼이_삭제할때_Y로_들어갈것인지() {
        MemberYN memberYN = new MemberYN("test");
        em.persist(memberYN);
        em.remove(memberYN);
        em.flush();

        MemberYN find = em.find(MemberYN.class, memberYN.getTestNo());
        Object o = em.createNativeQuery("SELECT * FROM TEST_MEMYN WHERE test_no = 1", MemberYN.class).getSingleResult();
        assertThat(((MemberYN)o).getDelYn(), is("Y"));
        List<MemberYN> delfind = em.createQuery("Select my from MemberYN my where my.testNo = 1", MemberYN.class).getResultList();
        assertThat(delfind.size(), is(0)); //JPQL은 soft delete조건이 먹는다. singleResult하믄 오류 나서 List형으로 수정.
        assertThat(find, nullValue());
    }


    @Test
    void 자식관계도_softDELTE가_작동하나요() {
        MemberYN memberYN = new MemberYN("이름");
        MembershipAddress address = new MembershipAddress("주소", "디테일");
        memberYN.addAddress(address);
        em.persist(memberYN);
        em.flush();

        em.remove(memberYN);
        em.flush();

        MembershipAddress find = em.find(MembershipAddress.class, address.getAddressNo());
        assertThat(find, nullValue());
    }
}