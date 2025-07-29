DECLARE @dday INTEGER,
        @sdate VARCHAR(10)
SET @dday = :dday
SET @sdate = Convert(VARCHAR(10), dateadd(dd, @dday, getdate()), 102)

--SELECT @sdate
SELECT a.patno, b.patname, a.meddept, a.rsvdate as rsvdate
     --, Convert(varchar(12), STR_REPLACE(LTRIM(b.telno2), '-', NULL)) as phoneno
     , '01072006269' as phoneno
     , Convert(varchar(15), '15222700') as sendno
     , Convert(varchar(10), NULL) as meddrno, Convert(varchar(10), NULL) as meddrnm
     , Convert(varchar(10), NULL) as depttogo, Convert(varchar(10), NULL) as deptname
     , NULL as ord_floor, a.remarks, Convert(varchar(10), NULL)as cemarks
     , 'N' as dayhosp
     , CASE WHEN a.sugacode in (
              'R600917G', 'R600918G', 'R600919G', 'R600920G', 'R600921G',
              'R600922G', 'R600930G', 'R600932G', 'R600933G', 'R600934G') THEN '2'
          WHEN isnull(a.rsvdr,'0') =  '2' THEN '2'
          WHEN isnull(a.rsvdr,'0') =  'B' THEN '4'
          WHEN isnull(a.rsvdr,'0') =  'C' THEN '5'
          ELSE '3' END examtype
     , CASE WHEN a.remarks not like '%취소%' AND
                 a.remarks not like '%보류%'
            THEN 'Y' ELSE 'N' END as sendyn
FROM  (SELECT a.patno, a.rsvdate, a.meddept, a.rsvdr, a.sugacode, a.workdate, a.meddate
	       , a.remark as remarks, a.userid
	   FROM ocs.dbo.mdoexamt a(INDEX mdoexamt_index_1)
	   WHERE a.rsvdate >= @sdate
	     AND a.rsvdate <  DATEADD(dd, 1, @sdate)
	     AND IsNull(rTrim(a.rtnyn),'N') = 'N'
          AND IsNull(rTrim(a.jinstat),'A') < 'C'
          AND a.sugagrp ='BR1'
	     AND a.patno NOT LIKE 'TS%'
          AND EXISTS(SELECT 1 FROM srxgubnt rb JOIN cocodemt cc on cc.codegubn = '799' AND cc.commcode = rb.rsvgubn WHERE rb.sugacode = a.sugacode)
          AND EXISTS(SELECT 1 FROM srxvlogs WHERE rsvid = a.rsvid AND IsNull(rTrim(stat), 'N') = 'N')
	     AND NOT EXISTS(SELECT 1 FROM coholymt WHERE holiday = @sdate)) a
	       JOIN ocs.dbo.appatbat b ON b.patno = a.patno
