package org.snudh.test.queryexec;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.Utils.FileUtil;
import org.snudh.ocs.MediBook;
import org.snudh.test.dto.PatientDto;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Configuration
public class OcsQueryService {

  private static final Logger log = LoggerFactory.getLogger(OcsQueryService.class);

  @PersistenceUnit(unitName = "OCSPersistence")
  EntityManagerFactory ocsemf;

  @PersistenceContext(unitName="OCSPersistence")
  private EntityManager em;


  public record PatientInfo(String patno, String patname) { }

  public void test_01() {
    EntityManager oem = ocsemf.createEntityManager();

    TypedQuery<PatientInfo> query = oem.createQuery(
            "SELECT patno, patname FROM Patient t WHERE t.patno = :patno",
            PatientInfo.class);

    Optional<PatientInfo> p1 = Optional.ofNullable(
            query.setParameter("patno", "01000005").getSingleResult());
  }

  public void test_02() {
    EntityManager oem = ocsemf.createEntityManager();

    Query query = oem.createNativeQuery(
            "SELECT patno, patname FROM appatbat t WHERE t.patno = :patno",
            PatientInfo.class);

    //List<PatientInfo> pp = query.setParameter("patno", "01000005").getResultList();
    Optional<PatientInfo> p1 = Optional.ofNullable(
            (PatientInfo)query.setParameter("patno", "01000005").getSingleResult() );
    //PatientInfo p1 = query.setParameter("patno", "01000005").getSingleResult();
  }

  public void test_03() {
    Query query = em.createNativeQuery(
            "SELECT patno, patname FROM appatbat t WHERE t.patno = :patno",
            PatientInfo.class);

    //List<PatientInfo> pp = query.setParameter("patno", "01000005").getResultList();
    Optional<PatientInfo> p1 = Optional.ofNullable(
            (PatientInfo)query.setParameter("patno", "01000005").getSingleResult() );
    p1.ifPresent(obj -> {
      log.info(obj.patno + " " + obj.patname);
    });
  }

  public Optional<PatientDto> test_04() {
    String sql = FileUtil.getFileAsString("queries/test.sql");
    Query query = em.createNativeQuery(sql, PatientDto.class);

    Optional<PatientDto> p1 = Optional.ofNullable(
            (PatientDto)query.setParameter("patno", "01000005").getSingleResult() );
    p1.ifPresent(obj -> {
      log.info(obj.patno() + " " + obj.patname());
    });
    return p1;
  }

  public List<PatientDto> test_05(Integer dday) {
    String sql = FileUtil.getFileAsString("queries/MediBooks.sql");
    Query query = em.createNativeQuery(sql, PatientDto.class);

    return query.setParameter("dday", dday).getResultList();
  }

  public List<MediBook> test_15(Integer dday) {
    String sql = FileUtil.getFileAsString("queries/MediBooks.sql");
    Query query = em.createNativeQuery(sql, MediBook.class);

    List<MediBook> pp = query.setParameter("dday", dday).getResultList();
    return pp;
  }

  public List<MediBook> test_16(Integer dday) {
    String sql = FileUtil.getFileAsString("queries/ExamBooks.sql");
    Query query = em.createNativeQuery(sql, MediBook.class);

    List<MediBook> pp = query.setParameter("dday", dday).getResultList();
    return pp;
  }
}
