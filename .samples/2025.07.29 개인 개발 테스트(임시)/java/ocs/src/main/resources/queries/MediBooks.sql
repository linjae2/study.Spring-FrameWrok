DECLARE @dday INTEGER,
        @sdate VARCHAR(10)
SET @dday = :dday
SET @sdate = Convert(VARCHAR(10), dateadd(dd, @dday, getdate()), 102)

--SELECT @sdate
SELECT a.patno, b.patname, a.meddept, a.rsvdate
     --, Convert(varchar(12), STR_REPLACE(LTRIM(b.telno2), '-', NULL)) as phoneno
     , '01072006269' as phoneno
     , Convert(varchar(15),
         CASE WHEN (isnull(a.remarks,'')  like '%특진실%') or (isnull(a.remarks,'')  like '%저경클리닉%')  THEN  '0262563367'
              WHEN (a.meddept ='03') THEN CASE WHEN (UPPER(ltrim(a.remarks)) like '%MINOR%') THEN '15222700'
                                               WHEN (ltrim(a.remarks) like '%진단%') or (ltrim(a.remarks) like '%진준%') THEN '15222700'
                                               ELSE '15222700' END
              ELSE IsNull(e.codename, '15222700')  END) as sendno
     , a.rsvdr meddrno
     , CASE WHEN (a.meddept ='29') and  (a.rsvdr = 'B0001') THEN ''
            WHEN (a.meddept ='29') and  (UPPER(SubString(a.remarks, 1, 2)) = 'OP') then ''
            WHEN (a.meddept ='37') and  (a.rsvdr = 'B0001') THEN ''
            WHEN (a.meddept ='37') and  (UPPER(SubString(a.remarks, 1, 2)) = 'OP') then ''
            WHEN (a.meddept ='03') and isnull(a.studr,'') <> '' THEN  f.hname
            ELSE c.hname + CASE WHEN c.groupb IN ('01', '04') THEN '교수' END END as meddrnm
     , CASE WHEN (a.remarks like '%원스톱2층%') THEN '원스톱협진센터(2층)'
          WHEN (isnull(a.remarks,'')  like '%특진실%') OR (isnull(a.remarks,'')  like '%저경클리닉%')  THEN '저경클리닉(3층)'
          WHEN (a.meddept ='03')  THEN CASE WHEN UPPER(ltrim(a.remarks)) LIKE '%MINOR%' THEN '6층외래수술실'
                                                  ELSE '구강외과(1층)'  END
          WHEN (a.meddept ='01')  THEN '구강내과(2층)'
          WHEN (a.meddept ='04')  THEN '치주과(4층)'
          WHEN (a.meddept ='05')  THEN CASE WHEN (ltrim(a.remarks) like '%중장구%') THEN '신관 2층 중앙장애인구강진료센터'
                                  ELSE '치과보존과(4층)'  END
          WHEN (a.meddept ='06')  THEN '치과보철과(5층)'
          WHEN (a.meddept ='07')  THEN '치과교정과(3층)'
          WHEN (a.meddept ='08')  THEN CASE WHEN (UPPER(ltrim(a.remarks)) like '%[[]GA]%') OR
                                                 (UPPER(ltrim(a.remarks)) like '%[[]IV SED]%') THEN '신관 3층 외래마취진료실'
                                  ELSE '소아치과(1층)' END
          WHEN (a.meddept ='43')  THEN '신관 3층(외래/마취 진료실)'
          WHEN (a.meddept ='42')  THEN '신관(장애인치과병원) 3층'
          WHEN (a.meddept ='11')  THEN '원내생진료센터(1층)'
          WHEN (a.meddept ='19')  THEN '2층 언어재활교육실'
          WHEN (a.meddept ='21')  THEN '임플란트센터(3층)'
          WHEN (a.meddept ='24')  THEN '턱교정수술센터(1층)'
          WHEN (a.meddept ='23')  THEN '스페셜클리닉(1층)'
          WHEN (a.meddept ='26')  THEN '노인구강진료실(1층)'
          WHEN (a.meddept ='27')  THEN '구강암진료실(1층)'
          WHEN (a.meddept ='28')  THEN '구강건강증진실(1층)'
          WHEN (a.meddept ='34')  THEN '치과임상시험센터(8층)'
          WHEN (a.ord_floor = '2B') THEN '원스톱협진센터(2층)'
          WHEN (a.ord_floor = '2C') THEN '원스톱협진센터(★신관★4층)'
          WHEN (d.deptname LIKE '원스톱협진센터%') THEN '원스톱협진센터(3층)'
          WHEN (a.meddept ='41')  THEN CASE WHEN (UPPER(ltrim(a.remarks)) like '%3FC%') OR (UPPER(ltrim(a.remarks)) like '%3FO%') THEN '신관 3층 중앙장애인구강진료센터'
                                  ELSE '신관 2층 중앙장애인구강진료센터' END
          ELSE d.deptname END depttogo
     , d.deptname
     , a.ord_floor, a.remarks, a.call_remarks as cemarks

     , CASE When (a.meddept ='03') AND (ltrim(a.remarks) like '%낮병동입원%') Then 'Y' ELSE 'N' END as dayhosp

     /***************************************************************************
     2022.06.27 modify  ILBAN_EXAMT_YN(일반검사 유무 체크)
     ***************************************************************************/
     , (SELECT CASE WHEN count(*) >= 1 THEN 'Y' ELSE 'N' END
        FROM mdoexamt WHERE rsvdate >= @sdate AND rsvdate < DATEADD(dd, +1, @sdate)
          AND isnull(rtrim(rsvid),'N') = 'N' AND patno = a.patno
          -- 치주예약의, 임플처방의 동일한 경우 일반검사예약 안내 발송
          AND ((meddept = a.meddept) OR (CASE WHEN (meddept = '21') AND (orddr = a.rsvdr) THEN '04' ELSE meddept END = a.meddept))
          AND isnull(rtnyn,'N') = 'N' AND isnull(rcptype, 'N') ='N' AND isnull(jinstat, 'A') < 'C' AND sugagrp = 'BR1') examtype
     , CASE WHEN a.remarks not like '%취소%'AND
                 a.remarks not like '%보류%'AND
                 a.call_remarks not like '%보류%'
            THEN 'N' ELSE 'Y' END as sendyn
FROM  (SELECT a.patno, a.admdate, a.rsvdate, a.meddept, a.rsvdr, a.spcdryn, a.workdate, a.meddate
	        , a.remarks, a.rejtyn, a.chair_no, a.count30, a.origin_rsv_date, a.origin_rsv_dr, a.userid
	        , a.jubtgubn, a.study_yn, a.studr, a.lendpflg, a.confirmyn, a.confirmid, a.confirmdate, a.rsvgubn
	        , a.pat_gubn, a.call_yn, a.call_remarks, a.ord_floor
	   FROM ocs.dbo.mdoptrvt a(INDEX idx_mdoptrvt_rsvdate)
	   WHERE a.rsvdate >= @sdate
	     AND a.rsvdate <  DATEADD(dd, 1, @sdate)
	     AND a.patno NOT LIKE 'TS%' AND IsNull(rTrim(a.rejtyn), 'N') = 'N'
	     AND NOT EXISTS(SELECT 1 FROM coholymt WHERE holiday = @sdate)) a
	       JOIN ocs.dbo.appatbat b ON b.patno = a.patno
	       JOIN ocs.dbo.cousermt c ON c.userid = a.rsvdr AND c.groupw = 'DO' AND c.workdept is NOT NULL
	       JOIN ocs.dbo.codeptmt d ON d.meddept = a.meddept
  	  LEFT JOIN ocs.dbo.cocodemt e ON e.codegubn = '087' AND e.commcode = a.meddept
  	  LEFT JOIN ocs.dbo.cousermt f ON f.userid = a.studr
