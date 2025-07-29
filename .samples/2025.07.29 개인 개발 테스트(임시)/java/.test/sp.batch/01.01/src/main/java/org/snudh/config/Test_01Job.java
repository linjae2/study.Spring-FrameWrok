package org.snudh.config;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.student.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Test_01Job {
  private final static Logger log = LoggerFactory.getLogger(Test_01Job.class);

  private static int reader = 0;
  private static int process = 0;
  private static int writer = 0;

  @Bean
  public Job test_01Job(JobRepository jobRepository, Step test_01Step1) {
    return new JobBuilder("test_01Job", jobRepository)
              .start(test_01Step1)
              .build();
  }

  @Bean
  public Step test_01Step1(
    JobRepository jobRepository,
    Tasklet helloStep1Tasklet1,
    PlatformTransactionManager platformTransactionManager
  ) {
    return new StepBuilder("helloStep1Tasklet1", jobRepository)
              .<Student, Student>chunk(10, platformTransactionManager)  // 10개 단위 처리
              .reader(new ItemReader<Student>() {
                @Override
                public Student read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException
                {
                  Thread.sleep(1000);
                  System.out.println(" #### Reader " + (++reader) + "번");
                  return new Student();
                }
              })
              .build();
  }


}
