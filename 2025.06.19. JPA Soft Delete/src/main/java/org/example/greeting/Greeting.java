package org.example.greeting;

import org.example.global.DeletedAtConverter;
import org.example.global.envelop.audit.AuditListener;
import org.hibernate.annotations.SoftDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@SoftDelete(
    columnName = "deleted_at",
    converter = DeletedAtConverter.class) // hibernate 지원 annotation
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Greeting {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean howAreYou;
  private Boolean iAmFine;
  private Boolean thankYouAndYou;
  private Boolean iAmFineThankYouSoMuch;

  @Builder
  private Greeting(
          final Boolean howAreYou,
          final Boolean iAmFine,
          final Boolean thankYouAndYou,
          final Boolean iAmFineThankYouSoMuch) {
      this.howAreYou = howAreYou;
      this.iAmFine = iAmFine;
      this.thankYouAndYou = thankYouAndYou;
      this.iAmFineThankYouSoMuch = iAmFineThankYouSoMuch;
  }

  public static Greeting ofOnlyIAmFine() {
    return Greeting.builder()
      .howAreYou(true)
      .iAmFine(true)
      .thankYouAndYou(true)
      .iAmFineThankYouSoMuch(false)
      .build();
  }

  public static Greeting ofOnlyYouAreFine() {
    return Greeting.builder()
      .howAreYou(true)
      .iAmFine(false)
      .thankYouAndYou(true)
      .iAmFineThankYouSoMuch(true)
      .build();
  }

  // 모두가 서로 인사를 건냈는가
  public boolean isEveryoneGreetingTheOther() {
      return this.howAreYou && this.thankYouAndYou;
  }

  // 모두가 좋은 하루를 보내고 있는가
  public boolean isEveryoneHavingGoodDay() {
      return this.iAmFine && this.iAmFineThankYouSoMuch;
  }

  public void updateToBeHappy() { // 강제로 모두 Happy 하도록 업데이트
    this.iAmFine = true;
    this.iAmFineThankYouSoMuch = true;
  }
}

