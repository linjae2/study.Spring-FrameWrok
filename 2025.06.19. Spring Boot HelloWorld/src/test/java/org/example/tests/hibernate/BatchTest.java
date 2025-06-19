package org.example.tests.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.example.tests.support.QueryCountUtil;
import org.example.tests.support.context.JpaTestContext;
import org.hibernate.annotations.BatchSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EnableJpaRepositories(considerNestedRepositories = true)
public class BatchTest extends JpaTestContext {
  @Autowired
  DeskRepository deskRepository;

  @Test
  @DisplayName("@BatchSize 애노테이션을 사용하면 N+1이 안터지고 1번만 로딩한다.")
  @Rollback(value = false)
  void findAll_loadCountIsZero() {
    // given
    long id = saveDeskWithThreePen();

    // when
    Desk desk = deskRepository.findById(id).get();
    for (Pen pen : desk.getPens()) {
      System.out.println(pen.getId());
    }

    // then
    long loadCount = QueryCountUtil.getEntityLoadCount(em);
    // 지연 로딩으로 Entity를 조회하지 않는다.
    assertEquals(1, loadCount);
  }

  private long saveDeskWithThreePen() {
      Desk desk = new Desk();
      desk.setPens(List.of(
          new Pen(),
          new Pen(),
          new Pen(),
          new Pen()
      ));
      deskRepository.save(desk);
      em.flush();
      em.clear();
      return desk.getId();
  }

  @Getter
  @Entity //@Table(name = "DESK_TABLE")
  @AllArgsConstructor
  @NoArgsConstructor
  static private class Desk {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Pen> pens = new ArrayList<>();
  }

  @Getter
  @Entity //@Table(name = "PEN_TABLE")
  @AllArgsConstructor
  @NoArgsConstructor
  static private class Pen {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desk desk;
  }

  static private interface DeskRepository extends JpaRepository<Desk, Long> {
    List<Desk> findAll();
  }
}
