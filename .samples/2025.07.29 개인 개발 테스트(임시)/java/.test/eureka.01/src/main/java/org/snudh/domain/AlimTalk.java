package org.snudh.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alim_talk")
@Builder
public class AlimTalk {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 발송등록일시
  private LocalDate reg_dt;

  // 발송등록순번
  private LocalDate reg_sn;

  //@Column(length = 20)  // YYYYMMDDHHmmSS
  private String sendDate;

  // 발신전화번호(필수)
  private String callbackNumber;

  // 국가번호 기본값은 82
  private String nationPhoneNumber;

  // 사용자 전화번호(app_user_id 중 하나는 필수)
  private String phoneNumber;

  // 앱유저아이디(phone_number 중 하나는 필수)
  private String appUserId;

  private String tmplCode;

  // 사용자에게 전달된 메시지
  @Column(columnDefinition="TEXT")
  private String message;

  // 템플릿 내용 중 강조 표기할 핵심 정보
  private String title;

  // 메시지 상단에 표기할 제목
  private String header;

  // 메세지에 첨부할 버튼
  private String attachment;

  // 전환전송 유형
  private String tranType;

  // 전환전송 메시지
  @Column(columnDefinition="TEXT")
  private String tranMessage;

  // LMS/MMS 전송시 제목
  private String subject;

  // 메세지 처리 상태
  // (0:완료, 1 : 초기, 2 : 전송, 3: 결과 대기)
  private Integer status;

  // 처리 결과
  private String proc_rs;

  // 참조 번호
  private String refKey;

  public static AlimTalk of(RsvInfoDto ri) {
    return AlimTalk.builder()
           .callbackNumber(ri.getToSendno())
           .build();
  }
}
