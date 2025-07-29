package org.snudh.config;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.student.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Test_01Job {
  private final static Logger log = LoggerFactory.getLogger(Test_01Job.class);

  private static int reader = 0;
  private static int process = 0;
  private static int writer = 0;

  @Bean
  public Job test01Job(JobRepository jobRepository, Step test_01Step1) {
    return new JobBuilder("test01Job", jobRepository)
              .start(test_01Step1)
              .build();
  }

  @Bean
  public Step test01Step1(
    JobRepository jobRepository,
    PlatformTransactionManager platformTransactionManager
  ) {
    return new StepBuilder("test01Step", jobRepository)
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
      .writer(new ItemWriter<Student>() {
        @Override
        public void write(Chunk<? extends Student> chunk) throws Exception {
          System.out.println("### Writer "+(++writer)+"번");
        }
      })
      //.taskExecutor(taskExecutor()) // 
      .build();
  }

    // 10개 프로세스 병렬 처리
    @Bean
    public TaskExecutor taskExecutor() {
      SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
      asyncTaskExecutor.setConcurrencyLimit(10);
      return asyncTaskExecutor;
    }
}
