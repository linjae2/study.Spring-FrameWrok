package org.snudh.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.DrRsvInfo;
import org.snudh.domain.DrSchedule;
import org.snudh.domain.RsvInfo;
import org.snudh.domain.TestRsvDr;
import org.snudh.utils.FileUtil;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Service
public class RsvService {
  private static final Logger log = LoggerFactory.getLogger(RsvService.class);

  @PersistenceContext
  private EntityManager em;

  public List<RsvInfo> getRsvMedis(Integer dDay) {
    log.info("Execute {} 일전 진료 예약 정보", dDay);
    String sql = FileUtil.getFileAsString("queries/MediBooks.sql");
    Query query = em.createNativeQuery(sql, RsvInfo.class);

    return query.setParameter("dday", dDay).getResultList();
  }

  public List<RsvInfo> getRsvExams(Integer dDay) {
    log.info("Execute {} 일전 검사 예약 정보", dDay);
    String sql = FileUtil.getFileAsString("queries/ExamBooks.sql");
    Query query = em.createNativeQuery(sql, RsvInfo.class);

    return query.setParameter("dday", dDay).getResultList();
  }

  public List<LocalDate> getHolyDays(YearMonth ym) {
    //log.info("Execute {} 일전 검사 예약 정보", dDay);
    //String sql = FileUtil.getFileAsString("queries/ExamBooks.sql");
    Query query = em.createNativeQuery("""
DECLARE @ym VARCHAR(10),
        @cd VARCHAR(10)
SET @ym = :ym + '.01', @cd = convert(VARCHAR(10), dateadd(dd, 1, getdate()), 102)
IF @ym > @cd SET @cd = @ym
SELECT holiday
FROM coholymt WHERE holiday >= @cd
AND holiday < dateadd(mm, 1, @ym)
UNION SELECT CONVERT(DATE, '9999.12.31')
""", LocalDate.class);

    return query.setParameter("ym", ym.format(DateTimeFormatter.ofPattern("yyyy.MM"))).getResultList();
  }

  public List<DrSchedule> getDrSchedule(String meddr, String meddept, YearMonth ym) {
    //log.info("Execute {} 일전 검사 예약 정보", dDay);
    //String sql = FileUtil.getFileAsString("queries/ExamBooks.sql");
    Query query = em.createNativeQuery("""
DECLARE @ym VARCHAR(10),
        @cd VARCHAR(10)
SET @ym = :ym + '.01', @cd = convert(VARCHAR(10), dateadd(dd, 1, getdate()), 102)
IF @ym > @cd SET @cd = @ym

SELECT meddate, am_schedule, pm_schedule
    , IsNull(rTrim(am_bookyn), 'N') am_bookyn, IsNull(rTrim(pm_bookyn), 'N') pm_bookyn, am_usechair, pm_usechair
FROM x_schedule WHERE meddr = :meddr AND meddept = :meddept
  AND meddate >= @cd AND meddate < dateadd(mm, 1, @ym)
UNION
SELECT ISNull(MIN(meddate), CONVERT(DATE, '9999.12.31')), NULL, NULL, 'N', 'N', 0, 0
FROM x_schedule WHERE meddr = :meddr AND meddept = :meddept
  AND meddate >= dateadd(mm, 1, @ym)
ORDER BY meddate
""", DrSchedule.class);

    return query.setParameter("ym"     , ym.format(DateTimeFormatter.ofPattern("yyyy.MM")))
                .setParameter("meddr"  , meddr)
                .setParameter("meddept", meddept)
                .getResultList();
  }

  public List<DrRsvInfo> getDrRsvInfos(String meddr, String meddept, YearMonth ym) {
    //log.info("Execute {} 일전 검사 예약 정보", dDay);
    //String sql = FileUtil.getFileAsString("queries/ExamBooks.sql");
    Query query = em.createNativeQuery("""
DECLARE @ym VARCHAR(10),
        @cd VARCHAR(10)
SET @ym = :ym + '.01', @cd = convert(VARCHAR(10), dateadd(dd, 1, getdate()), 102)
IF @ym > @cd SET @cd = @ym

SELECT rsvdate, IsNull(tx_time, 30) tx_time, IsNull(rTrim(overlap), 'Y') overlap
FROM mdoptrvt
WHERE rsvdate >= @cd AND rsvdate < dateadd(mm, 1, @ym)
  AND rsvdr = :meddr AND meddept = :meddept
  AND IsNull(rTrim(rejtyn), 'N') = 'N'
UNION
SELECT IsNull(MIN(rsvdate), CONVERT(DATE, '9999.12.31')), 0, 'Y'
FROM mdoptrvt WHERE rsvdate >= dateadd(mm, 1, @ym)
  AND rsvdr = :meddr AND meddept = :meddept
ORDER BY rsvdate
""", DrRsvInfo.class);

    return query.setParameter("ym"     , ym.format(DateTimeFormatter.ofPattern("yyyy.MM")))
                .setParameter("meddr"  , meddr)
                .setParameter("meddept", meddept)
                .getResultList();
  }

  public List<TestRsvDr> getTestRsvDr() {
    //log.info("Execute {} 일전 검사 예약 정보", dDay);
    //String sql = FileUtil.getFileAsString("queries/ExamBooks.sql");
    Query query = em.createNativeQuery("""
DECLARE @ym VARCHAR(10),
        @cd VARCHAR(10)
SET @ym = '2024.08' + '.01', @cd = convert(VARCHAR(10), dateadd(dd, 1, getdate()), 102)
IF @ym > @cd SET @cd = @ym

SELECT DISTINCT rsvdr, meddept, IsNull(tx_time, 30) tx_time, IsNull(rTrim(overlap), 'Y') overlap
FROM mdoptrvt
WHERE rsvdate >= @cd --AND rsvdate < dateadd(mm, 1, @ym) 
  AND IsNull(rTrim(rejtyn), 'N') = 'N'
ORDER BY rsvdate
""", TestRsvDr.class);

    return query.getResultList();
  }
}
