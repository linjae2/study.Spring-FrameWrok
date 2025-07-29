package org.snudh.api;

import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.cron.Cron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.jobs.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

  private Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/run")
  public String runJob(
    @RequestParam(value = "d-day", defaultValue = "1") Integer dday
  ) {
    BackgroundJob.<JobService>enqueue(JobService -> System.out.println("test!"));

    BackgroundJob.<JobService>enqueue(jobService -> jobService.sendEmail("01", "02", "03", "04", "05"));
    return "Job is enqueued.";
  }

  @GetMapping("/mediAlim")
  public String runJob1(
    @RequestParam(value = "d-day", defaultValue = "1") Integer dday
  ) {
    BackgroundJob.<JobService>enqueue(jobService -> jobService.mediAlim(dday));
    return "Job is enqueued.";
  }

  @GetMapping("/cron")
  public String registerCron(
    @RequestParam(value = "d-day", defaultValue = "1") Integer dday
  ) {
    return BackgroundJob.<JobService>scheduleRecurrently(
      String.format("MedAlim__%02d", dday),
      //Cron.daily(10),
      "0/1 * * * *",
      jobService -> jobService.mediAlim(dday));
    //return "Job is enqueued.";
  }
}
