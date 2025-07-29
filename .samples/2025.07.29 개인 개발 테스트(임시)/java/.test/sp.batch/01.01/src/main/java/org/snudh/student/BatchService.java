package org.snudh.student;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchService {
  private final JobLauncher jobLauncher;
  private final Job helloJob;
  private final Job test_01Job;

  public void runSimpleJob() {
    try {
        JobParameters jobParameters = new JobParametersBuilder()
                .toJobParameters();
        jobLauncher.run(helloJob, jobParameters);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  public void runTest_01Job() {
    try {
        JobParameters jobParameters = new JobParametersBuilder()
                .toJobParameters();
        jobLauncher.run(test_01Job, jobParameters);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
