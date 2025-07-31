package org.snudh.psv.domain;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.SQLException;

import lombok.*;
import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.NaturalId;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.SelectGenerator;
import org.hibernate.id.enhanced.Optimizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@EnableJpaRepositories(considerNestedRepositories = true)
public class TestService {
  @PersistenceUnit
  EntityManagerFactory emf;

  @PersistenceContext
  private EntityManager em;

  public void test() {
    test01();
    //test0201();
  }

  public void test01() {
//    TsMember member1 = new TsMember("abcd");
//
//    // ====================================================================
//    EntityManager em = emf.createEntityManager();
//    EntityTransaction tx = em.getTransaction();
//
//    tx.begin();
//    try {
//      em.persist(member1);
//      tx.commit();
//    } finally {
//      em.close();
//    }
  }

  public void test021() {

  }

  static class TsMember {
    private Long id;
  }

//  @Entity @Data
//  // @EqualsAndHashCode(onlyExplicitlyIncluded = true)
//  @NoArgsConstructor
//  // @NoArgsConstructor(access = AccessLevel.PROTECTED)
//  @GenericGenerator(name = "triggered", type = SelectGenerator.class)
//  static class TsMember {
//    @Id // @Generated
//    // @EqualsAndHashCode.Include
//    @GeneratedValue(generator = "triggered")
//    private Long id;
//
//    @NaturalId
//    // @Column(name = "product_key", nullable = false, updatable = false, unique = true)
//    private String name;
//
//    public TsMember(String name) {
//      this.name = name;
//    }
//  }
}

@Data
@NoArgsConstructor
class MyEntity {
  private Long id;
  private String name;

  public MyEntity(String name) {
    this.name = name;
  }
}