package org.snudh.domain;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface SchedAlimRepository extends JpaRepository<SchedAlim, SchedAlim.Key> {

  // WHERE m.alim.execDay >= DATEADD('DAY', -2, CURRENT_DATE())
  // @Query("SELECT exists(SELECT 1 FROM SchedAlim m\n"
  //      + "WHERE m.alim.execDay >= SUBDATE(CURRENT_DATE(), 'INTERVAL 2 DAY'))")
  //@Query("SELECT exists(SELECT 1 FROM SchedAlim m WHERE m.alim.execDay >= CURRENT_DATE())")
  // @Query("SELECT exists(SELECT 1 FROM SchedAlim m\n"
  //      + "WHERE m.alim.execDay >= FUNCTION('DATEADD', 'DAY', -2, CURRENT_DATE()))")
  @Query("SELECT exists(SELECT 1 FROM SchedAlim m\n"
       + "WHERE m.cdate >= (CURRENT_DATE - 102 DAY)\n"
       + "  AND m.msg_st > 0)")
  public boolean existsPopedData();

  @Query("SELECT m FROM SchedAlim m\n"
       + "WHERE m.cdate >= (CURRENT_DATE - 102 DAY)\n"
       + "  AND m.msg_st > 0\n"
       + "ORDER BY m.cdate limit 1")
  public SchedAlim topData();

  @Modifying
  @Transactional
  @Query("UPDATE SchedAlim m SET m.msg_st = :msg_st\n"
       + "WHERE m.key.rsvDate = :rsvDate\n"
       + "  AND m.key.dDay = :dDay\n"
       + "  AND m.key.msgType = :msgType\n"
       + "  AND m.key.seq = :seq\n")
  public void StatusUpdate(
     @Param("rsvDate"  )LocalDate rsvDate,
     @Param("dDay"     )Integer dDay,
     @Param("msgType"  )String msgType,
     @Param("seq"      )Integer seq,
     @Param("msg_st"   )Integer msg_st);
}

// private LocalDate rsvDate;     // 예약(진료/검사)일
// private Integer   dDay;       // 진료 디데이(알림발송)
// private String    msgType;    // 진료/검사 예약
// private Integer   seq;        // 키 순번
