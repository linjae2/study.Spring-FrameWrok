package org.snudh.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class HwanService {

  private static final Logger log = LoggerFactory.getLogger(HwanService.class);

  @PersistenceContext
  private EntityManager em;

  private String getPaInfoQuery(LocalDate meddate, String meddept, String meddr) {
    String mdept = meddept;
    if ("37".equals(mdept) || "38".equals(mdept) || "39".equals(mdept) || "40".equals(mdept)) mdept = "29";
    String sql = """
SELECT CASE WHEN pat_visittime IS NULL THEN 99999999999 ELSE seqno END seqno
     , rsvdate, pat_visit, pat_chairno, patname, patno
     , pat_called, medyn, hname, pat_hold, pat_endtime, jubtgubn, jubtgubn2
     , CASE WHEN pat_visittime IS NULL THEN NULL ELSE pat_calledtime END pat_calledtime
     , CASE WHEN meddept = '29' AND meddr = 'Z0328' THEN 'Z0028' ELSE meddr END meddr
     , pat_room, pat_priority, pat_visittime, pat_selcall, workdate
     , meddate, rcpdate, rcphmm
     , fstmedty, rsvgubn
     , pat_check
FROM pa_info
""";
    sql += "WHERE RSVDATE like '" + meddate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "%'";
    sql += "\n  AND meddept = '" + mdept + "'";

    if ("29".equals(mdept)) {  // 원스톱협진센터
      if (meddr.startsWith("Z")) {
        if ("Z0028".equals(meddr) || "Z0328".equals(meddr)) {
          // 원스톱 3층에서 스케일링환자를 구증 1, 구증 2로 접수
          sql += "\n  AND meddr IN ('Z0028', 'Z0328')";
        } else
        {
          sql += "\n  AND meddr like 'Z%'";
          sql += "\n  AND meddr NOT IN ('Z0028', 'Z0328')";
        }
      } else
      {
        sql += "\n  AND NVL(jubtgubn, '0') <> '1'";
        sql += "\n  AND NVL(nochcd, '00') <> '22'";
        sql += "\n  -- AND meddr = '" + meddr + "'";
      }
    } else
    {
      if (meddr.startsWith("Z")) {
        sql += "\n  AND meddr like 'Z%'";
      } else
      {
        sql += "\n  AND NVL(jubtgubn, '0') <> '1'";
        sql += "\n  AND NVL(nochcd, '00') <> '22'";
        sql += "\n  AND meddr = '" + meddr + "'";
      }
    }
    sql += "\nORDER BY pat_hold, pat_calledtime desc, seqno desc";
    return sql;
  }


  public Character getVisitCheckValue(LocalDate meddate, String patno, String meddept) {
    Character ch = null;
    String sql = "SELECT PAT_VISIT FROM pa_info";
    sql += "\nWHERE RSVDATE like '" + meddate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "%'";
    sql += "\n  AND PATNO = :patno";
    sql += "\n  AND MEDDEPT = :meddept";

    Query query = em.createNativeQuery(sql, Character.class);

    @SuppressWarnings("unchecked")
    List<Character> rs = query
            .setParameter("patno", patno)
            .setParameter("meddept", meddept)
            .getResultList();
    if (!rs.isEmpty()) ch = rs.get(0);
    return ch;
  }

  public Integer getStandbyInteger(LocalDate meddate, String meddept, String meddr) {
    String sql = "WITH tmp_info AS (\n" + getPaInfoQuery(meddate, meddept, meddr) + ")";
    sql += "\nSELECT count(1) FROM tmp_info a";
    sql += "\nWHERE nvl(pat_called,'N') = 'N'";
    sql += "\n  AND nvl(pat_visit, 'N') = 'Y'";
    sql += "\n  AND pat_calledtime IS NULL";

    System.out.println(sql);
    Query query = em.createNativeQuery(sql, Integer.class);
    return (Integer)query.getSingleResult();
  }
}
