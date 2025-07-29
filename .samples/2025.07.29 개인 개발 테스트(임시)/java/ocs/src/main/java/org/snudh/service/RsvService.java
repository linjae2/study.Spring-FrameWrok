package org.snudh.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.DrMonthSch;
import org.snudh.domain.DrRsvInfo;
import org.snudh.domain.DrSchedule;
import org.snudh.domain.RsvDrInfo;
import org.snudh.domain.RsvInfo;
import org.snudh.domain.TestRsvDr;
import org.snudh.utils.FileUtil;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class RsvService {
  private static LocalDate LASTDT = LocalDate.of(9000, 1, 1);
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

  public byte[] getDaySchedule(String meddr, String meddept, LocalDate date, int tx_time, char overlap) {
    YearMonth ym = YearMonth.from(date);

    int usechair = 0;                 // 당일 예약 가능 총 인원
    byte[] schedule = new byte[157];  // +1한 이유는 while 시 null 에러 방지

    // == 5분 단위 스케쥴 초기화 ==
    int idx = 0;
    for(idx = 0; idx < 157; ++idx) schedule[idx] = 0;

    // == 진료의 스케줄 ==
    List<DrSchedule> drschedule = getDrSchedule(meddr, meddept, ym);
    Iterator<DrSchedule> itDrs = drschedule.iterator();
    DrSchedule drs = itDrs.next();

    while(date.isAfter(drs.medddate)) drs = itDrs.next();

    if (date.isEqual(drs.medddate)) {
      // == 병원 휴일 ==
      List<LocalDate> hols = getHolyDays(ym);
      Iterator<LocalDate> itHols = hols.iterator();
      LocalDate hol = itHols.next();

      // == 진료의 예약정보 ==
      List<DrRsvInfo> drRsvInfos = getDrRsvInfos(meddr, meddept, ym);
      Iterator<DrRsvInfo> itRsv = drRsvInfos.iterator();
      DrRsvInfo rsv = itRsv.next();

      // 이전 휴일 정보 skip
      while(date.isAfter(hol)) hol = itHols.next();

      // 이전 예약 정보 skip
      while(date.isAfter(rsv.rsvdate.toLocalDate())) rsv = itRsv.next();

      if (!date.equals(hol) && // 병원 휴일 이면 skip
            date.equals(rsv.rsvdate.toLocalDate())) {

        // 해당 시간에 예약 가능 인원을 셋 한다.
        if (drs.am_bookyn == 'Y' && drs.am_usechair > 0) // 오전 스케줄이 있으면.
        for(idx =  0; idx <  72; ++idx) {
          if (drs.am_schedule.length() > idx && drs.am_schedule.charAt(idx) != '-') {
            usechair += drs.am_usechair;
            schedule[idx     ] = (byte)drs.am_usechair;
          };
        };

        if (drs.pm_bookyn == 'Y' && drs.pm_usechair > 0) // 오후 스케줄이 있으면.
        for(idx = 0; idx < 84; ++idx) {
          if (drs.pm_schedule.length() > idx && drs.pm_schedule.charAt(idx) != '-') {
            usechair += drs.pm_usechair;
            schedule[idx + 72] = (byte)drs.pm_usechair;
          };
        };
        // == 5분 단위 스케쥴 초기화 END ==

        // 스케줄에 등록된 예약자 수 계산
        // =====================================
        if (usechair > 0) do {
          LocalTime lt = rsv.rsvdate.toLocalTime();
          int loc = (lt.getHour() - 7) * 12 + lt.getMinute() / 5;
          int len = rsv.tx_time / 5;

          while (len-- > 0) {
            if (schedule[loc + len] > 0) {
              // 중복 예약 불가시 예약 가능을 0으로 고정
              if (rsv.overlap != 'Y' || overlap != 'Y') {
                usechair -= schedule[loc + len];
                schedule[loc + len] = 0;
              } else
              // Over 부킹 여부
              if (schedule[loc + len] > 0) {
                usechair--;
                schedule[loc + len]--;
              }
            }
          }
          rsv = itRsv.next();
        } while(date.equals(rsv.rsvdate.toLocalDate()));
      }
    }

    System.out.println(Arrays.toString(schedule));
    System.out.println("====================================================================================");
    return schedule;
  }

  public DrMonthSch getSchedule(String meddr, String meddept, YearMonth ym, int tx_time, char overlap) {
    int[] days = null;
    boolean rsExist = false;
    LocalDate nextmed = null;

    if (ym != null) {
      rsExist = true;
    } else
    {ym = YearMonth.now();}

    while(true) {
      // 기초 데이터 초기화
      int mDays = ym.lengthOfMonth();
      days = new int[mDays];
      int dIdx = 0;
      while(dIdx < mDays) { days[dIdx++] = 0; }

      // == 진료의 스케줄 ==
      List<DrSchedule> drschedule = getDrSchedule(meddr, meddept, ym);
      Iterator<DrSchedule> itDrs = drschedule.iterator();
      DrSchedule drs = itDrs.next();

      LocalDate date= ym.atDay(mDays);

      if (!date.isBefore(drs.medddate)) {
        // == 병원 휴일 ==
        List<LocalDate> hols = getHolyDays(ym);
        Iterator<LocalDate> itHols = hols.iterator();
        LocalDate hol = itHols.next();

        // == 진료의 예약정보 ==
        List<DrRsvInfo> drRsvInfos = getDrRsvInfos(meddr, meddept, ym);
        Iterator<DrRsvInfo> itRsv = drRsvInfos.iterator();
        DrRsvInfo rsv = itRsv.next();

        LocalDate curd = drs.medddate;
        do {
          // 이전 휴일 정보 skip
          while(curd.isAfter(hol)) hol = itHols.next();

          // 이전 예약 정보 skip
          while(curd.isAfter(rsv.rsvdate.toLocalDate())) rsv = itRsv.next();

          if (!curd.equals(hol) && // 병원 휴일 이면 skip
               curd.equals(rsv.rsvdate.toLocalDate())) {
            dIdx = curd.getDayOfMonth() - 1;
            int usechair = 0;                 // 당일 예약 가능 총 인원
            byte[] schedule = new byte[157];  // +1한 이유는 while 시 null 에러 방지

            // == 5분 단위 스케쥴 초기화 ==
            // 해당 시간에 예약 가능 인원을 셋 한다.
            int idx = 0;
            for(idx = 0; idx < 157; ++idx) schedule[idx] = 0;

            if (drs.am_bookyn == 'Y' && drs.am_usechair > 0) // 오전 스케줄이 있으면.
            for(idx =  0; idx <  72; ++idx) {
              if (drs.am_schedule.length() > idx && drs.am_schedule.charAt(idx) != '-') {
                usechair += drs.am_usechair;
                schedule[idx     ] = (byte)drs.am_usechair;
              };
            };

            if (drs.pm_bookyn == 'Y' && drs.pm_usechair > 0) // 오후 스케줄이 있으면.
            for(idx = 0; idx < 84; ++idx) {
              if (drs.pm_schedule.length() > idx && drs.pm_schedule.charAt(idx) != '-') {
                usechair += drs.pm_usechair;
                schedule[idx + 72] = (byte)drs.pm_usechair;
              };
            };
            // == 5분 단위 스케쥴 초기화 END ==

            // 스케줄에 등록된 예약자 수 계산
            // =====================================
            if (usechair > 0) do {
              LocalTime lt = rsv.rsvdate.toLocalTime();
              int loc = (lt.getHour() - 7) * 12 + lt.getMinute() / 5;
              int len = rsv.tx_time / 5;

              while (len-- > 0) {
                if (schedule[loc + len] > 0) {
                  // 중복 예약 불가시 예약 가능을 0으로 고정
                  if (rsv.overlap != 'Y' || overlap != 'Y') {
                    usechair -= schedule[loc + len];
                    schedule[loc + len] = 0;
                  }
                  else {
                    usechair--;
                    schedule[loc + len]--;
                  }
                }
              }
              rsv = itRsv.next();
            } while(curd.equals(rsv.rsvdate.toLocalDate()));

            // 예약 가능 시간 존재 여부 체크
            // =====================================
            if (usechair > 0) {
              int len = tx_time / 5;
              int sIdx = -1, eIdx = -1;
              for(idx = 0; idx < 157;) {
                if (schedule[idx] > 0) {
                  if (sIdx < 0) sIdx = idx;
                } else
                if (sIdx >= 0 && schedule[idx] == 0) {
                  eIdx = idx - len;
                  if (sIdx <= eIdx) {
                    if (idx < 72) {     // 오전
                      days[dIdx] |= 1;
                      idx = 71;
                    } else {            // 오후
                      days[dIdx] |= 2;
                      break;
                    }
                  }
                  sIdx = -1; eIdx = -1;
                }
                idx++;
              }
            }

            // 예약 가능한 날짜가 있음
            if (!rsExist) rsExist = days[dIdx] != 0;
          };
          drs = itDrs.next();
          curd = drs.medddate;
        } while(!date.isBefore(curd));
      } else {
        rsExist = LASTDT.isBefore(drs.medddate);
      }
      System.out.println(Arrays.toString(days));
      System.out.println("====================================================================================");
      nextmed = drs.medddate;

      if (rsExist) break;
      ym = YearMonth.from(nextmed);
      //ym = ym.plusMonths(1);
    }

    return new DrMonthSch(ym, nextmed, days);
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
SET @ym = '2024.10' + '.01', @cd = convert(VARCHAR(10), dateadd(dd, 1, getdate()), 102)
IF @ym > @cd SET @cd = @ym

SELECT DISTINCT rsvdr, meddept, IsNull(tx_time, 30) tx_time, IsNull(rTrim(overlap), 'Y') overlap
FROM mdoptrvt
WHERE rsvdate >= @cd AND rsvdate < dateadd(mm, 1, @ym)
  AND IsNull(rTrim(rejtyn), 'N') = 'N'
ORDER BY rsvdate
""", TestRsvDr.class);

    return query.getResultList();
  }

  public List<RsvDrInfo> getRsvInfo(String patno, LocalDateTime meddate, String meddept) {
    Query query = em.createNativeQuery("""
SELECT um.userid rsvdr, um.hname drname
     , dm.deptname, IsNull(rv.tx_time, 0) tx_time, IsNull(rTrim(rv.overlap), 'N') overlap
     , IsNull(rTrim(rv.change_online), 'N') change_yn
     --, rv.change_online, rv.change_center, *
FROM mdoptrvt rv JOIN cousermt um ON um.userid = rv.rsvdr
                 JOIN codeptmt dm ON dm.meddept = rv.meddept
WHERE rv.patno = :patno AND rv.rsvdate = :meddate AND rv.meddept = :meddept
  AND isNull(rTrim(rv.rejtyn), 'N') = 'N'
""", RsvDrInfo.class);

    return query.setParameter("patno", patno)
                .setParameter("meddate", meddate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
                .setParameter("meddept", meddept)
                .getResultList();
  }
}
