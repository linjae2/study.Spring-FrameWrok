package com.violetbeach.hibernateplayground.hibernate;

import com.violetbeach.hibernateplayground.person.Person;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ElementCollectionTest extends JpaTestContext {

    @Test
    @DisplayName("@ElementCollection는 콜렉션 Attribute까지 영속화한다.")
    void elementCollection_ItPersistCollectionAttributes() {
        // given
        Person expectedPerson = new Person();
        String expectedAddress = "분당 판교";
        expectedPerson.setAddresses(List.of(expectedAddress));

        // when
        em.persist(expectedPerson);
        em.flush();
        em.clear();

        // then
        Person person = em.find(Person.class, expectedPerson.getId());
        List<String> addresses = person.getAddresses();
        assertThat(addresses.get(0)).isEqualTo(expectedAddress);
    }
    
}
