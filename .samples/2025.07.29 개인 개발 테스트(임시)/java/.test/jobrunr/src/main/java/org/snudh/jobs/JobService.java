package org.snudh.jobs;

import org.jobrunr.jobs.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Job(name = "%0일전 진료 예약 알림")
  public void mediAlim(int dday) {
    logger.info("{}일전 진료 예약 안내 발송", dday);
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        logger.error("Error while executing sample job", e);
    } finally {
        logger.info("Sample job has finished...");
    }
  }

  @Job(name = "%0일전 검사 예약 알림")
  public void examAlim(int dday) {
    logger.info("{}일전 검사 예약 안내 발송", dday);
    //try {
    //    Thread.sleep(3000);
    //} catch (InterruptedException e) {
    //    logger.error("Error while executing sample job", e);
    //} finally {
    //    logger.info("Sample job has finished...");
    //}
  }


  @Job(name = "Sending email to %2 (requestId: %X{request.id})", labels = {"tenant:%0", "email"})
  public void sendEmail(String tenant, String from, String to, String subject, String body) {
      // business code here
  }
}
