package org.snudh.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.AlimTalk;
import org.snudh.domain.AlimTmpl;
import org.snudh.domain.AlimTmplRepository;
import org.snudh.domain.RsvAlim;
import org.snudh.domain.RsvAlimRepository;
import org.snudh.domain.RsvInfoDto;
import org.snudh.domain.SchedAlim;
import org.snudh.domain.SchedAlimRepository;
import org.snudh.domain.SchedDto;
import org.snudh.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlimService {
  private static final Logger log = LoggerFactory.getLogger(AlimService.class);

  //@PersistenceContext
  //private EntityManager em;
  @PersistenceUnit
  private final EntityManagerFactory emf;
  
  private final RsvAlimRepository rsvRepository;
  private final SchedAlimRepository alimRepository;
  private final AlimTmplRepository tmplRepository;


  public void putSchedAlim(SchedDto data) {
    //log.info("Execute {} 일전 진료 예약 정보", dDay);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    SchedAlim alim = SchedAlim.of(data);
    tx.begin();
    try {
      alim.getKey().setExecDay(LocalDate.now());
      em.persist(alim);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw e;
    }
  }

  public boolean existsPopedData() {
    return alimRepository.existsPopedData();
    //return true;
  }

  public SchedAlim topData() {
    return alimRepository.topData();
    //return true;
  }

  public void putRsvAlim(SchedAlim.Key key, RsvInfoDto data) {
    //log.info("Execute {} 일전 진료 예약 정보", dDay);

    RsvAlim ra
      //          = rsvRepository.getRsvAlim(
      // key.getRsvDay(), key.getDDay(), key.getMsgType(),
      // data.getPatno(), data.getRsvdate(), data.getMeddept())
      ;

    //if (rsvRepository.existsRsvAlim1())

    if (!rsvRepository.existsRsvAlim(
                  key.getRsvDay(), key.getDDay(), key.getMsgType(),
                  data.getPatno(), data.getRsvdate(), data.getMeddept()
    ))
    {
      Integer seq = rsvRepository.getMaxSequence(key.getRsvDay(), key.getDDay(), key.getMsgType());
      if (seq == null) seq = 0;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();

      ra = RsvAlim.of(key.getRsvDay(), key.getDDay(), key.getMsgType(), seq + 1, data);
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
      ra = rsvRepository.getRsvAlim(
                  key.getRsvDay(), key.getDDay(), key.getMsgType(),
                  data.getPatno(), data.getRsvdate(), data.getMeddept());
      
    }
    // SchedAlim alim = SchedAlim.of(data);
    // tx.begin();
    // try {
    //   alim.getKey().setExecDay(LocalDate.now());
    //   em.persist(alim);
    //   tx.commit();
    // } catch (Exception e) {
    //   tx.rollback();
    //   throw e;
    // }
  }


  public void putMediRsvTalk(RsvInfoDto data) {
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
{"button":[{"name":"진찰권보기","type":"WL","url_pc":"https://sv.snudh.org/MediInfo/CARD/<JWT>}", "url_mobile":"https://sign.snudh.org/Account/Login?ReturnUrl=tesetes55"}]}""", null));
    }
    
    ST st = new ST(tmpl.getTx());
    st.add("medi", data);

    String msg = st.render(new Locale("ko"));
    System.out.println("Runner Bean #1:" + msg);  // 1
    
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    AlimTalk alimTalk = AlimTalk.of(data);
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

}
