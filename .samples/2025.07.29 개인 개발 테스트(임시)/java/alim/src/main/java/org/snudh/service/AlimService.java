package org.snudh.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.AlimTalk;
import org.snudh.domain.AlimTalkRepository;
import org.snudh.domain.AlimTmpl;
import org.snudh.domain.AlimTmplRepository;
import org.snudh.domain.PatToken;
import org.snudh.domain.PatTokenRepository;
import org.snudh.domain.RsvAlim;
import org.snudh.domain.RsvAlimRepository;
import org.snudh.domain.RsvInfoDto;
import org.snudh.domain.SchedAlim;
import org.snudh.domain.SchedAlimRepository;
import org.snudh.domain.SchedDto;
import org.snudh.event.RsvAlimEvent;
import org.snudh.provider.JwtTokenProvider;
import org.snudh.utils.FileUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stringtemplate.v4.ST;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlimService {
  private static final Logger log = LoggerFactory.getLogger(AlimService.class);

  //@PersistenceContext
  //private EntityManager em;
  @PersistenceUnit
  private final EntityManagerFactory emf;

  private final ApplicationEventPublisher eventPublisher;

  private final JwtTokenProvider tokenProvider;

  private final RsvAlimRepository rsvRepository;
  private final AlimTalkRepository alimTalkRepository;
  private final SchedAlimRepository alimRepository;
  private final AlimTmplRepository tmplRepository;
  private final PatTokenRepository patTokenRepository;


  private int get_rsv_alim_seq(EntityManager em, SchedDto data) {
    StoredProcedureQuery spq = em.createStoredProcedureQuery("event_rsv_alim_seq");
    spq.registerStoredProcedureParameter(1, LocalDate.class, ParameterMode.IN);
    spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
    spq.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
    spq.registerStoredProcedureParameter(4, Integer.class, ParameterMode.OUT);

    spq.setParameter(1, data.getRsvDate());
    spq.setParameter(2, data.getDday());
    spq.setParameter(3, data.getMsgType());
    spq.execute();

    return (int)spq.getOutputParameterValue(4);
  }

  public void putSchedAlim(SchedDto data) {
    //log.info("Execute {} 일전 진료 예약 정보", dDay);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    SchedAlim alim = SchedAlim.of(data);
    tx.begin();
    try {
      //alim.getKey().setExecDay(LocalDate.now());
      alim.getKey().setSeq(get_rsv_alim_seq(em, data));
      em.persist(alim);
      tx.commit();
      eventPublisher.publishEvent(new RsvAlimEvent());
    } catch (Exception e) {
      tx.rollback();
      throw e;
    } finally {
      em.close();
      log.info("=================== close ======================");
    }
  }

  public boolean existsAlimPopedData() {
    return alimRepository.existsPopedData();
    //return true;
  }

  public SchedAlim topData() {
    return alimRepository.topData();
    //return true;
  }

  public boolean existsAlimTalkPopedData() {
    return alimTalkRepository.existsPopedData();
    //return true;
  }

  @Transactional
  public void updateSchedAlim(SchedAlim.Key key, Integer state)
  {
    alimRepository.StatusUpdate(key.getRsvDate(), key.getDDay(), key.getMsgType(), key.getSeq(), state);
  }

  /*
   * 발송예정일에 정상 발송 여부에 따라 발송 자료 생성
   */
  public void distributRsvAlim(
    LocalDate rsvDate,     // 예약(진료/검사)일
    Integer   dDay,        // 진료 디데이(알림발송)
    String    msgType,     // 진료/검사 예약
    RsvInfoDto data
  ) {
    if (dDay == 0 ||
        !rsvRepository.existsRsvAlim(rsvDate, dDay, msgType,
            data.getPatno(), data.getRsvdate(), data.getMeddept()
    )) {
      Integer seq = rsvRepository.getMaxSequence(rsvDate, dDay, msgType);
      if (seq == null) seq = 0;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();

      RsvAlim ra = RsvAlim.of(rsvDate, dDay, msgType, seq + 1, data);
      tx.begin();
      try {
        em.persist(ra);
        tx.commit();
      } catch (Exception e) {
        tx.rollback();
        //throw e;
      } finally {
        em.close();
      }
    }
  }

  public RsvAlim putRsvAlim(SchedAlim.Key key, RsvInfoDto data) {
    //log.info("Execute {} 일전 진료 예약 정보", dDay);

    RsvAlim ra = null;
    if (!rsvRepository.existsRsvAlim(
                  key.getRsvDate(), key.getDDay(), key.getMsgType(),
                  data.getPatno(), data.getRsvdate(), data.getMeddept()
    ))
    {
      Integer seq = rsvRepository.getMaxSequence(key.getRsvDate(), key.getDDay(), key.getMsgType());
      if (seq == null) seq = 0;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();

      ra = RsvAlim.of(key.getRsvDate(), key.getDDay(), key.getMsgType(), seq + 1, data);
      tx.begin();
      try {
        em.persist(ra);
        tx.commit();
      } catch (Exception e) {
        tx.rollback();
        //throw e;
      } finally {
        em.close();
      }
    } else
    {
      // ra = rsvRepository.getRsvAlim(
      //             key.getRsvDate(), key.getDDay(), key.getMsgType(),
      //             data.getPatno(), data.getRsvdate(), data.getMeddept());

    }
    return ra;
  }


  public void putMediRsvTalk(RsvInfoDto data, AlimTalk alimTalk) {
    AlimTmpl tmpl = tmplRepository.findById("진료").orElse(null);
    if (tmpl == null)  {
      tmpl = tmplRepository.save(new AlimTmpl("진료", "mts032", """
[서울대학교치과병원]
<medi.patname> (등록번호- <medi.patno>)님 <medi.toMeddate> <medi.deptname> <medi.toMeddr><medi.toEtc>

[예약 취소 및 변경]

전화 : <medi.toSendno>

■ 본인-신분증 확인 의무화(24년 5월 20일부터 신분증 지참 필수)

   불편하시더라도 꼭 신분증을 지참하여 주시길 부탁드립니다
   (※ 신분증 미지참시 진료비 전액을 본인부담하셔야 합니다)

● 진료비 자동결제(바로결제) 서비스 운영안내

  신용카드를 미리 수납창구에서 신청등록하시면 이후에는 수납창구 방문없이 진료비가 다음날 자동결제 됩니다

감사합니다.""", """
{"button":[
   {"name":"진찰권보기","type":"WL","url_pc":"https://sv.snudh.org/MediInfo/CARD/<JWT>}", "url_mobile":"https://sign.snudh.org/Account/Login?ReturnUrl=tesetes55"}
]}""", null));
    }

    ST st = new ST(tmpl.getTx());
    st.add("medi", data);
    String msg = st.render(new Locale("ko"));
    //System.out.println("Runner Bean #1:" + msg);  // 1

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    alimTalk.setTmplCode(tmpl.getCd());
    alimTalk.setMessage(msg);

    alimTalk.setSubject("예약알림");
    alimTalk.setTranType(msg.getBytes().length < 90? "S": "L");
    alimTalk.setTranMessage(msg);

    tx.begin();
    try {
      em.persist(alimTalk);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      //throw e;
    } finally {
      em.close();
    }
  }

  public PatToken generatorJwtV1(RsvInfoDto ri) {
    PatToken tc = PatToken.builder()
                          .patno(ri.getPatno())
                          .rsvdate(ri.getRsvdate())
                          .meddept(ri.getMeddept())
                          .patName(ri.getPatname())
                          .deptnm(ri.getDeptname())
                          .alimdrnm(ri.getMeddrnm())
                          .tokVer(1)
                          .build();

    tc.setExpired(ri.getRsvdate().toLocalDate().plusMonths(6));
    tc.setToken(tokenProvider.createToken(ri));

    System.out.println(tc.getToken());

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      em.persist(tc);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw e;
    } finally {
      em.close();
    }
    return tc;
    // SecretKey key = Jwts.SIG.HS256.key().build();
    // String secretString = Encoders.BASE64.encode(key.getEncoded());

    // byte[] keyBytes = Decoders.BASE64.decode(secretString);
    // SecretKey key01 = Keys.hmacShaKeyFor(keyBytes);

    // //LocalDateTime now = LocalDateTime.now();
    // ZoneId zone = ZoneId.systemDefault();
    // long issueEpoch = ri.getRsvdate().plusMonths(1).atZone(zone).toEpochSecond();

    // String token = Jwts.builder()
    //       .claim("ptn", ri.getPatno())
    //       .claim("mdt", ri.getRsvdate().atZone(zone).toEpochSecond())
    //       .claim("mdd", ri.getMeddept())
    //       .claim("iat", LocalDateTime.now().atZone(zone).toEpochSecond())
    //       .claim("exp", ri.getRsvdate().toLocalDate().plusMonths(6).plusDays(1).atStartOfDay(zone).toEpochSecond())
    //       .signWith(key)
    //       .compact();
    //String token = tokenProvider.createToken(ri);

    // Jwts.parser().setSigningKey(key01)
    // Jwts.parserBuilder().setSigningKey
    // JwtParser jwtParser = Jwts.parser().verifyWith(key).build();
    // Claims claims = jwtParser.parseSignedClaims(token).getPayload();

    // String ptn  = claims.get("ptn", String.class);
    // long _mdt   = claims.get("mdt", Long.class);
    // String mdd  = claims.get("mdd", String.class);
    // long _exp   = claims.get("exp", Long.class);

    // LocalDateTime mdt = Instant.ofEpochSecond(_mdt).atZone(zone).toLocalDateTime();
    // LocalDateTime exp = Instant.ofEpochSecond(_exp).atZone(zone).toLocalDateTime();

    // System.out.println("key ==> " + secretString);
    // System.out.println("key ==> " + Encoders.BASE64.encode(key01.getEncoded()));

    //System.out.println(token);
  }

  public PatToken findByToken(String token) {
    return patTokenRepository.findByToken(token);
  }

}
