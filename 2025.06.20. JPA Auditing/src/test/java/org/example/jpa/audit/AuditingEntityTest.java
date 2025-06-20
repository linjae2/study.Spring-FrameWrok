package org.example.jpa.audit;

import static org.mockito.Mockito.times;

import java.time.LocalDateTime;

import org.example.config.AuditingConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.test.annotation.Rollback;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DataJpaTest
@Import(AuditingConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuditingEntityTest {
  private static final Logger log = LoggerFactory.getLogger(AuditingEntityTest.class);

  @PersistenceContext
  private EntityManager em;

  @Test
  void main() {
    System.out.println("========================================");
  }

  @Getter
  @Entity @EntityListeners(AuditingEntityListener.class)
  @NoArgsConstructor
  static private class Post1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    private String name;

    public Post1(String name) {
      this.name = name;
    }

    @PrePersist
    public void prePersist() {
      System.out.println(" => Called prePersist");
    }
  }


  @Test
  @Rollback(value = false)
  void Post_Test01() {
    System.out.println("========================================");

    Post1 post1 = new Post1();
    Post1 post2 = new Post1();
    Post1 post3 = new Post1();
    Post1 post4 = new Post1();
    Post1 post5 = new Post1();
    em.persist(post1);
    em.persist(post2);
    em.persist(post3);
    em.persist(post4);

    post1.setName("abcde");
    em.persist(post5);
  }

  @Getter
  @MappedSuperclass
  @EntityListeners(AuditingEntityListener.class)
  static private class BaseTime {
    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
      System.out.println(" => Called prePersist");
    }
  }

  @Getter @Entity
  @NoArgsConstructor
  static private class Post2 extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @PrePersist
    public void prePersist() {
      System.out.println(" => Called prePersist");
    }
  }

  @Test
  @Rollback(value = false)
  void Post2_Test() {
    System.out.println("========================================");

    Post2 post1 = new Post2();
    Post2 post2 = new Post2();
    Post2 post3 = new Post2();
    Post2 post4 = new Post2();
    Post2 post5 = new Post2();
    em.persist(post1);
    em.persist(post2);
    em.persist(post3);
    em.persist(post4);

    post1.setName("abcde");
    em.persist(post5);
  }
}
