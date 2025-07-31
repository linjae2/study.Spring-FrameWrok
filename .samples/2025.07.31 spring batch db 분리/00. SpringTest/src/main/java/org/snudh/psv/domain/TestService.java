package org.snudh.psv.domain;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
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
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
@EnableJpaRepositories(considerNestedRepositories = true)
public class TestService {
  @PersistenceUnit
  EntityManagerFactory emf;

  @PersistenceContext
  private EntityManager em;

  private final UserReposityory reposityory;
  private final TsMember0Repository tsMember0Repository;
  private final TsMember1Repository tsMember1Repository;
  private final TsMember2Repository tsMember2Repository;



  @Transactional
  public void test01() {
    TsMember0 member1 = new TsMember0("abcd");

    // No EntityManager with actual transaction available for current thread
    //   - cannot reliably process 'persist' call
    em.persist(member1);
  }

  public void test02() {
    TsMember0 member1 = new TsMember0("abcd");

    // No EntityManager with actual transaction available for current thread
    //   - cannot reliably process 'persist' call
    em.persist(member1);
  }

  public void test03() {
    TsMember0 member1 = new TsMember0("abcd");

    // ====================================================================
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    tx.begin();
    try {
      em.persist(member1);
      tx.commit();
    } finally {
      em.close();
    }
  }

  @Transactional
  public void test21() {
    TsMember1 member1 = new TsMember1(10L, "abcd");
    TsMember0 member0 = new TsMember0("abcd");
    //tsMember1Repository.save(member1);
    tsMember0Repository.save(member0);
  }

  @Transactional
  public void test22() {
    TsMember2 member2 = new TsMember2("abcd");
    //tsMember1Repository.save(member1);
    tsMember2Repository.save(member2);
  }

  public void test9999() {
    User user = new User();
    user.setUserId("hong1");
    user.setUserName("hong gil dong");
    user.setAge(20);

    TsMember0 member1 = new TsMember0("abcd");

    tsMember0Repository.save(member1);
    reposityory.save(user);
  }

  public void test() {
    test22();
    //test0201();
  }

  @Getter @Setter
  @Entity
  static class TsMember0 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public TsMember0(String name) {
      this.name = name;
    }
  }

  @Entity @Data
  @NoArgsConstructor
  static class TsMember1 {
    @Id
    private Long id;

    private String name;

    public TsMember1(Long id, String name) {
      this.id = id;
      this.name = name;
    }
  }

  @Entity @Data
  @NoArgsConstructor
  static class TsMember2 {
    @Id
    @GeneratedValue(generator = "select1")
    private Long id;

    private String name;

    public TsMember2(String name) {
      this.name = name;
    }
  }


  static public interface TsMember0Repository extends JpaRepository<TsMember0, Long> {
  }

  static public interface TsMember1Repository extends JpaRepository<TsMember1, Long> {
  }

  static public interface TsMember2Repository extends JpaRepository<TsMember2, Long> {
  }


  // ===================================================================================

  public void test0201() {
    TestEntity t1 = new TestEntity();
    TestEntity t2 = new TestEntity();

    // ====================================================================
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    tx.begin();
    try {
      em.persist(t1);
      em.persist(t2);
      tx.commit();
    } finally {
      em.close();
    }
  }

  static private class CustomIdGenerator implements IdentifierGenerator {
    private static int counter = 1;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
      Connection connection = null;

      try {
        ConnectionProvider connectionProvider = session
            .getFactory()
            .getServiceRegistry()
            .getService(ConnectionProvider.class);
        connection = connectionProvider.getConnection();

        session.getEntityPersister(object.getClass().getName(), object);

      } catch (SQLException e) {

      }

      try {
        // 공유세션으로부터 jdbc connection을 얻는다
        connection = session.getJdbcConnectionAccess().obtainConnection();
      } catch (SQLException e) {
        throw new HibernateException(e);
      } finally {

      }

      String prefix = "USR";
      return prefix + String.format("%05d", counter++);
    }
  }

  @IdGeneratorType(CustomIdGenerator.class)
  @Target({METHOD, FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  static private @interface Sequence {
    // String name();
    // int startWith() default 1;
    // int incrementBy() default 50;
    // Class<? extends Optimizer> optimizer() default Optimizer.class;
  }


  // @Entity
  // static private class TestEntity01 {
  //   @Id
  //   @GenericGenerator(
  //     name = "idGenerator",
  //     type = CustomSequenceGenrator.class
  //   )
  //   @GeneratedValue(generator = "idGenerator")
  //   private String id;
  // }

  @Entity
  static private class TestEntity {
    @Id
    @Sequence
    private String id;
  }
}
