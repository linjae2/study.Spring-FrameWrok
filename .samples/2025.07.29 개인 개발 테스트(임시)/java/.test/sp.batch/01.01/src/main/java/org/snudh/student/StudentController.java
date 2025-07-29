package org.snudh.student;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
  private final JobLauncher jobLauncher;
  private final Job helloJob;
  private final BatchService batchService;

  @PostMapping
  public void importCSVtoDBJob() {
    JobParameters jobParameters = new JobParametersBuilder()
    .addLong("startAt", System.currentTimeMillis())
    .toJobParameters();

    try {
      jobLauncher.run(helloJob, jobParameters);
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }

  @GetMapping("/simple")
  @ResponseBody
  public String simple() {
    batchService.runSimpleJob();
    return "runSimpleJob OK";
  }

  @GetMapping("/test01")
  public String test01() {
    batchService.runTest_01Job();
    return "runSimpleJob OK";
  }
}
