create user 'sdman'@'%' identified by 'snudh!v123';
grant all privileges on *.* to 'sdman'@'%';
flush privileges;

-- Data too long for column 오류 해결
set @@global.sql_mode = "ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION";

CREATE
    DATABASE alim DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

use alim;


/*==============================================================*/
/*==============================================================*/
DROP TABLE IF EXISTS event_rsv_alim;
DROP TABLE IF EXISTS sch_alim_talk;

CREATE OR REPLACE TABLE event_rsv_alim
(
   rsv_date             Date            not null comment '예약 (진료/검사)일',
   d_day                Integer         not null comment '예약일 전 발송 DDay',
   msg_type             VARCHAR( 10)    not null comment '진료, 검사',
   seq                  Integer         not null comment '키 순번',
   msg_data             MEDIUMTEXT,
   msg_st               Integer         not null comment '처리 상태 => 1 : 초기 상태, 2 : 처리중, 0 : 처리 완료, -1 : 오류',
   cdate                DateTime        not null comment '생성일시',
   mdate                DateTime        not null comment '수정일시',
   primary key (rsv_date, d_day, msg_type, seq)
) default charset utf8 COMMENT "스케줄러";

CREATE OR REPLACE INDEX event_rsv_alim_idx01 on event_rsv_alim (cdate);

CREATE OR REPLACE TABLE event_rsv_alim_pk
(
   rsv_date             Date            not null comment '예약 (진료/검사)일',
   d_day                Integer         not null comment '예약일 전 발송 DDay',
   msg_type             VARCHAR( 10)    not null comment '진료, 검사',
   seq                  Integer         not null comment '키 순번',
   primary key (rsv_date, d_day, msg_type)
) default charset utf8 COMMENT "스케줄러 키";


CREATE OR REPLACE PROCEDURE event_rsv_alim_seq(
   IN  i_rsv_date             Date,
   IN  i_d_day                Integer,
   IN  i_msg_type             varchar( 10),
   OUT o_seq                  Integer
)
BEGIN
  START TRANSACTION;
  INSERT INTO event_rsv_alim_pk VALUES (i_rsv_date, i_d_day, i_msg_type, 1)
  ON DUPLICATE KEY UPDATE seq = seq + 1;

  SELECT seq into o_seq FROM event_rsv_alim_pk
  WHERE rsv_date = i_rsv_date and d_day = i_d_day
    AND msg_type = i_msg_type;

  COMMIT;
END;

/*==============================================================*/
-- alim.rsv_alim definition
CREATE OR REPLACE TABLE rsv_alim (
   rsv_date             Date            not null comment '예약 (진료/검사)일',
   d_day                Integer         not null comment '예약일 전 발송 DDay',
   msg_type             VARCHAR( 10)    not null comment '진료, 검사',
   seq                  Integer         not null comment '키 순번',


  patno 	   varchar(10) 	NOT NULL,
  patname 	varchar(255) 	NOT NULL,
  meddept 	varchar(  5)		 NULL,
  meddate	datetime     	NOT NULL,

  phoneno   varchar( 25)   NOT NULL,
  sendno    varchar( 25)   NOT NULL,

  meddrno 	varchar( 10)		NULL,
  alimdrnm 	varchar( 50)		NULL,
  deptname  varchar(100) 		NULL,
  depttogo  varchar(150) 		NULL,

  ordfloor  varchar( 15)      NULL,
  remarks   text              NULL,
  cemarks   text              NULL,

  dayhosp	char(1)			NOT NULL,
  examtype  char(1)			NOT NULL,
  sendyn	char(1) 		NOT NULL,

  cdate                DateTime        not null comment '생성일시',
  mdate                DateTime        not null comment '수정일시',
  primary key (rsv_date, d_day, msg_type, seq)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT "스케줄러";


/*==============================================================*/
/*==============================================================*/

CREATE OR REPLACE TABLE rsv_alim
(
   rsv_date             Date            not null comment '예약 (진료/검사)일',
   d_day                Integer         not null comment '예약일 전 발송 DDay',
   msg_type             VARCHAR( 10)    not null comment '진료, 검사',
   seq                  Integer         not null comment '키 순번',
   msg_data             MEDIUMTEXT,
   mst_st               Integer         not null comment '처리 상태 => 1 : 초기 상태, 2 : 처리중, 0 : 처리 완료, -1 : 오류',

   primary key (rsv_date, d_day, msg_type, seq)
) default charset utf8 COMMENT "스케줄러";


CREATE TABLE `` (
  `dayhosp` char(1) NOT NULL,
  `examtype` char(1) NOT NULL,
  `sendyn` char(1) NOT NULL,
  `rsvdate` datetime(6) DEFAULT NULL,
  `cemarks` varchar(255) DEFAULT NULL,
  `deptname` varchar(255) DEFAULT NULL,
  `meddept` varchar(255) DEFAULT NULL,
  `meddr` varchar(255) DEFAULT NULL,
  `ordfloor` varchar(255) DEFAULT NULL,
  `patname` varchar(255) DEFAULT NULL,
  `patno` varchar(255) DEFAULT NULL,
  `phoneno` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `sendno` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`d_day`,`rsv_day`,`sequence`,`msg_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE OR REPLACE TABLE rsv_alim_pk
(
   rsv_date             Date            not null comment '예약 (진료/검사)일',
   d_day                Integer         not null comment '예약일 전 발송 DDay',
   msg_type             VARCHAR( 10)    not null comment '진료, 검사',
   seq                  Integer         not null comment '키 순번',
   primary key (rsv_date, d_day, msg_type)
) default charset utf8 COMMENT "스케줄러 키";


CREATE OR REPLACE PROCEDURE rsv_alim_seq(
   IN  i_rsv_date             Date,
   IN  i_d_day                Integer,
   IN  i_msg_type             varchar( 10),
   OUT o_seq                  Integer
)
BEGIN
  START TRANSACTION;
  INSERT INTO rsv_alim_pk VALUES (i_rsv_date, i_d_day, i_msg_type, 1)
  ON DUPLICATE KEY UPDATE seq = seq + 1;

  SELECT seq into o_seq FROM rsv_alim_pk
  WHERE rsv_date = i_rsv_date and d_day = i_d_day
    AND msg_type = i_msg_type;

  COMMIT;
END;

/*==============================================================*/
/*==============================================================*/
CREATE OR REPLACE TABLE pat_token (
   id bigint not null auto_increment,

   patno          VARCHAR(10)    not null,
   rsvdate        DATETIME(6)    not null,
   meddept        VARCHAR( 5)    not null,

   pat_name       VARCHAR(50)    not null,
   deptnm         VARCHAR(50)    not null,
   alimdrnm		   VARCHAR(55)        null,

   tok_ver        INTEGER        not null,
   expired        DATE           not null,
   token          VARCHAR(1024)  not null,
   cdate          DATETIME(6)    not null,
   primary key (id)
) default charset utf8 COMMENT "jwt 토큰 정보";

CREATE OR REPLACE INDEX pat_tocken_idx01 on pat_token(token);
CREATE OR REPLACE INDEX pat_tocken_idx02 on pat_token(patno, rsvdate, meddept);