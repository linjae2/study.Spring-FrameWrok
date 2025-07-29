package org.snudh.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.SchedAlim;
import org.snudh.domain.SchedAlimRepository;
import org.snudh.domain.SchedDto;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {
  private static final Logger log = LoggerFactory.getLogger(TestService.class);

  @PersistenceUnit
  private final EntityManagerFactory emf;

  private final SchedAlimRepository alimRepository;


  @Transactional
  private int get_rsv_alim_seq(SchedDto data) {
    EntityManager em = emf.createEntityManager();
    StoredProcedureQuery spq = em.createStoredProcedureQuery("event_rsv_alim_seq");
    spq.registerStoredProcedureParameter(1, LocalDate.class, ParameterMode.IN);
    spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
    spq.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
    spq.registerStoredProcedureParameter(4, Integer.class, ParameterMode.OUT);

    spq.setParameter(1, data.getRsvDate());
    spq.setParameter(2, data.getDday());
    spq.setParameter(3, data.getMsgType());
    spq.execute();

    return (int)spq.getOutputParameterValue(4);
  }

  public void putSchedAlim(SchedDto data) {
    //log.info("Execute {} 일전 진료 예약 정보", dDay);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    SchedAlim alim = SchedAlim.of(data);
    tx.begin();
    try {
      //alim.getKey().setExecDay(LocalDate.now());
      alim.getKey().setSeq(get_rsv_alim_seq(data));
      em.persist(alim);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw e;
    }
  }

  public void test01(SchedDto data) {
    //log.info("Execute {} 일전 진료 예약 정보", dDay);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    tx.begin();
    try {
      StoredProcedureQuery spq = em.createStoredProcedureQuery("event_rsv_alim_seq");
      spq.registerStoredProcedureParameter(1, LocalDate.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(4, Integer.class, ParameterMode.OUT);

      spq.setParameter(1, data.getRsvDate());
      spq.setParameter(2, data.getDday());
      spq.setParameter(3, data.getMsgType());
      spq.execute();

      Integer out = (Integer)spq.getOutputParameterValue(4);

      System.out.println("out = " + out);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw e;
    }
  }
}
