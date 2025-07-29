package org.snudh;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
//import org.qlrm.mapper.JpaResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.ocs.MediBook;
import org.snudh.ocs.Patient;
import org.snudh.ocs.PatientRepository;
import org.snudh.test.queryexec.OcsQueryService.PatientInfo;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.context.annotation.Configuration
public class CommandLine_01 {
	
  @PersistenceUnit(unitName = "OCSPersistence")
  EntityManagerFactory ocsemf;

  @PersistenceContext(unitName="OCSPersistence")
  private EntityManager em;

  private static final Logger log = LoggerFactory.getLogger(Application.class);


  //@Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CommandLineRunner getRunnerBean1() {
      return args -> {
        System.out.println("Runner Bean #1");  // 1
      };
  }

  //@Bean
  public CommandLineRunner getRunnerBean2(PatientRepository patientRepository) {
      return args -> {
        System.out.println("Runner Bean #2");  // 2

			  // find book by ID
			  Optional<Patient> patient1 = patientRepository.findById("01000002");
        patient1.ifPresent(
          obj -> {
            log.info(obj.toString());

            Date date = Date.from(obj.getEditdate().atZone(ZoneId.systemDefault()).toInstant());

            System.out.println(date);
          });
      };
  }

  //@Bean
  public CommandLineRunner getRunnerBean3() {
      return args -> {
        System.out.println("Runner Bean #3");  // 2

        String date = "2024-05-08 17:23:56";
        String timestamp = "2020-05-01 12:30:00";

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //Date d1 = new Date(DATE_FORMAT.parse(date).getTime());
        //Timestamp t1 = new Timestamp(DATE_FORMAT.parse(date).getTime());
      };
  }

  public record PatientInfo(String patno, String patname, LocalDateTime editdate) { }
  //@Bean
  public CommandLineRunner getRunnerBean4() {
    return args -> {
      System.out.println("Runner Bean #4");  // 2

      //JpaResultMapper mapper = new JpaResultMapper();
      EntityManager oem = ocsemf.createEntityManager();

      Query query = oem.createNativeQuery(
              "SELECT patno, patname, editdate FROM appatbat t WHERE t.patno = :patno",
              PatientInfo.class).setParameter("patno", "01000005");
  
      //query.unwrap(NativeQuery.class).setResultListTransformer(null)
      //.setResultTransformer(Transformers.aliasToBean(PatientInfo.class));
      
      //List<PatientInfo> pp = query.getResultList();

      //List<PatientInfo> dtos = mapper.list(query, PatientInfo.class);

      // // //List<PatientInfo> pp = query.setParameter("patno", "01000005").getResultList();
      // // Optional<PatientInfo> p1 = Optional.ofNullable(
      // //         (PatientInfo)query.setParameter("patno", "01000005").getSingleResult() );
    };
  }

  //@Bean
  //@Order(1)
  public ApplicationRunner getRunnerBean00() {
      return args -> {
        System.out.println("Runner Bean #3");  // 3
        System.out.println("======================================================");
    };
  }

}
