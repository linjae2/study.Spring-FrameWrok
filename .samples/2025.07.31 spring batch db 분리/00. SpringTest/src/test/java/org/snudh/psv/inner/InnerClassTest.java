package org.snudh.psv.inner;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@SpringBootTest
@ActiveProfiles("test")
// @EnableJpaRepositories(considerNestedRepositories = true)
public class InnerClassTest {
  private static final Logger log = LoggerFactory.getLogger(InnerClassTest.class);

  // @Autowired
  // private TestIdentityRepository testIdentityRepository;


  @Test
  void main() {

  }

  // @Getter
  // @Entity
  // static class TestIdentity {
  //   @Id
  //   @GeneratedValue(strategy = GenerationType.IDENTITY)
  //   private Long id;

  //   private String Name;

  //   public TestIdentity(String Name) {
  //     this.Name = Name;
  //   }
  // }

  // static public interface TestIdentityRepository extends JpaRepository<TestIdentity, Long> {
  // }
}
