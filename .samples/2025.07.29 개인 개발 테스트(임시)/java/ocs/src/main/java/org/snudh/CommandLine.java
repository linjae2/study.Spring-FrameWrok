package org.snudh;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snudh.domain.DrRsvInfo;
import org.snudh.domain.DrSchedule;
import org.snudh.domain.TestRsvDr;
import org.snudh.service.RsvService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class CommandLine {

  //private final static Log log = LogFactory.getLog(CommandLine.class);
  private final static Logger log = LogManager.getLogger(CommandLine.class);
  private final RsvService service;

  static private int testval = 0;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CommandLineRunner getRunnerBean1() {
    log.info("> ========== Runner Bean #1 ========== ");
    System.out.println("Runner Bean #1");  // 2

    return args -> {
      if (testval == 0) return;
      List<TestRsvDr> rsvdrs = service.getTestRsvDr();
      int[] days = null;

      for (TestRsvDr e : rsvdrs) {
        String mddr = e.rsvdr, meddept = e.meddept;
        YearMonth ym = YearMonth.of(2024, 10);

        int tx_time = e.tx_time;
        char overlap = e.overlap;

        int mDays = ym.lengthOfMonth();
        days = new int[mDays];
        int dIdx = 0;
        while(dIdx < mDays) { days[dIdx++] = 0; }

        // == 진료의 스케줄 ==
        List<DrSchedule> drschedule = service.getDrSchedule(mddr, meddept, ym);
        Iterator<DrSchedule> itDrs = drschedule.iterator();
        DrSchedule drs = itDrs.next();

        LocalDate date = ym.atDay(mDays);

        if (!date.isBefore(drs.medddate))
        {
          // == 병원 휴일 ==
          List<LocalDate> hols = service.getHolyDays(ym);
          Iterator<LocalDate> itHols = hols.iterator();
          LocalDate hol = itHols.next();

          // == 진료의 예약정보 ==
          List<DrRsvInfo> drRsvInfos = service.getDrRsvInfos(mddr, meddept, ym);
          Iterator<DrRsvInfo> itRsv = drRsvInfos.iterator();
          DrRsvInfo rsv = itRsv.next();

          LocalDate curd = drs.medddate;
          do {
            // 이전 휴일 정보 skip
            while(curd.isAfter(hol)) hol = itHols.next();

            // 이전 예약 정보 skip
            while(curd.isAfter(rsv.rsvdate.toLocalDate())) rsv = itRsv.next();

            if (!curd.equals(hol) && // 병원 휴일 이면 skip
                 curd.equals(rsv.rsvdate.toLocalDate())) {
              dIdx = curd.getDayOfMonth() - 1;
              byte[] schedule = new byte[157];  // +1한 이유는 while 시 null 에러 방지

              // == 5분 단위 스케쥴 초기화 ==
              int idx = 0;
              for(idx = 0; idx < 157; ++idx) schedule[idx] = 0;

              if (drs.am_bookyn == 'Y' && drs.am_usechair > 0) // 오전 스케줄이 있으면.
              for(idx =  0; idx <  72; ++idx) {
                if (drs.am_schedule.length() > idx && drs.am_schedule.charAt(idx) != '-') schedule[idx     ] = (byte)drs.am_usechair;
              };

              if (drs.pm_bookyn == 'Y' && drs.pm_usechair > 0) // 오후 스케줄이 있으면.
              for(idx = 0; idx < 84; ++idx) {
                if (drs.pm_schedule.length() > idx && drs.pm_schedule.charAt(idx) != '-') schedule[idx + 72] = (byte)drs.pm_usechair;
              };
              // == 5분 단위 스케쥴 초기화 END ==

              do {
                LocalTime lt = rsv.rsvdate.toLocalTime();
                int loc = (lt.getHour() - 7) * 12 + lt.getMinute() / 5;
                int len = rsv.tx_time / 5;

                while (len-- > 0) {
                  if (schedule[loc + len] > 0) {
                    // 중복 예약 불가시 예약 가능을 0으로 고정
                    if (rsv.overlap != 'Y' || overlap != 'Y') schedule[loc + len] = 0;
                    else {
                      schedule[loc + len]--;
                    }
                  }
                }

                rsv = itRsv.next();
              } while(curd.equals(rsv.rsvdate.toLocalDate()));

              int len = tx_time / 5;
              int sIdx = -1, eIdx = -1;
              for(idx = 0; idx < 157;) {
                if (schedule[idx] > 0) {
                  if (sIdx < 0) sIdx = idx;
                } else
                if (sIdx >= 0 && schedule[idx] == 0) {
                  eIdx = idx - len;
                  if (sIdx <= eIdx) {
                    if (idx < 72) {     // 오전
                      days[dIdx] |= 1;
                      idx = 71;
                    } else {            // 오후
                      days[dIdx] |= 2;
                      break;
                    }
                  }
                  sIdx = -1; eIdx = -1;
                }
                idx++;
              }
            };
            drs = itDrs.next();
            curd = drs.medddate;
          } while(!date.isBefore(curd));
        }
        //   for (dIdx = 0; dIdx < mDays; dIdx++) {
        //     date = ym.atDay(dIdx + 1);

        //     if (date.compareTo(hol) != 0 &&   // 병원 휴일
        //     date.compareTo(drs.medddate) == 0) {
        //       byte[] schedule = new byte[157];  // +1한 이유는 while 시 null 에러 방지

        //       // == 5분 단위 스케쥴 초기화 ==
        //       int idx = 0;
        //       for(idx = 0; idx < 157; ++idx) schedule[idx] = 0;

        //       if (drs.am_bookyn == 'Y' && drs.am_usechair > 0) // 오전 스케줄이 있으면.
        //       for(idx =  0; idx <  72; ++idx) {
        //         if (drs.am_schedule.length() > idx && drs.am_schedule.charAt(idx) != '-') schedule[idx     ] = (byte)drs.am_usechair;
        //       };

        //       if (drs.pm_bookyn == 'Y' && drs.pm_usechair > 0) // 오후 스케줄이 있으면.
        //       for(idx = 0; idx < 84; ++idx) {
        //         if (drs.pm_schedule.length() > idx && drs.pm_schedule.charAt(idx) != '-') schedule[idx + 72] = (byte)drs.pm_usechair;
        //       };
        //       // == 5분 단위 스케쥴 초기화 END ==

        //       // 이전 예약 정보 skip
        //       while(date.isAfter(rsv.rsvdate.toLocalDate())) rsv = itRsv.next();

        //       while(date.compareTo(rsv.rsvdate.toLocalDate()) == 0) {
        //         LocalTime lt = rsv.rsvdate.toLocalTime();
        //         int loc = ((lt.getHour() - 7) * 60 + lt.getMinute()) / 5;
        //         int len = rsv.tx_time / 5;

        //         while (len > 0) {
        //           if (schedule[loc + len] > 0) {
        //             // 중복 예약 불가시 예약 가능을 0으로 고정
        //             if (rsv.overlap != 'Y' || overlap != 'Y') schedule[loc + len] = 0;
        //             else {
        //               schedule[loc + len]--;
        //             }
        //           }
        //           len--;
        //         }
        //         rsv = itRsv.next();
        //       }

        //       int len = tx_time / 5;
        //       int sIdx = -1, eIdx = -1;
        //       for(idx = 0; idx < 157;) {
        //         if (schedule[idx] > 0) {
        //           if (sIdx < 0) sIdx = idx;
        //         } else
        //         if (sIdx >= 0 && schedule[idx] == 0) {
        //           eIdx = idx - len;
        //           if (sIdx <= eIdx) {
        //             if (idx < 72) {     // 오전
        //               days[dIdx] |= 1;
        //               idx = 71;
        //             } else {            // 오후
        //               days[dIdx] |= 2;
        //               break;
        //             }
        //           }
        //           sIdx = -1; eIdx = -1;
        //         }
        //         idx++;
        //       }
        //     }
        //   }
        // }

        // List<LocalDate> hols = service.getHolyDays(ym);
        // List<DrRsvInfo> drRsvInfos = service.getDrRsvInfos(mddr, meddept, ym);

        // Iterator<LocalDate> itHols = hols.iterator();
        // LocalDate hol = itHols.next();

        // Iterator<DrRsvInfo> itRsv = drRsvInfos.iterator();
        // DrRsvInfo rsv = itRsv.next();

        // while(dIdx < mDays) {
        //   days[dIdx] = 0;
        //   LocalDate date= ym.atDay(dIdx + 1);
        //   if (date.compareTo(hol) != 0 &&
        //       date.compareTo(drs.medddate) == 0) {

        //     // System.out.println(String.format("%s %s(%s)",
        //     //   date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
        //     //   mddr, meddept
        //     // ));

        //     byte[] schedule = new byte[157];  // +1한 이유는 while 시 null 에러 방지
        //     int idx = 0;
        //     for(idx = 0; idx < 157; ++idx) schedule[idx] = 0;
        //     if (drs.am_bookyn == 'Y' && drs.am_usechair > 0) for(idx =  0; idx <  72; ++idx) {
        //       // 오전 스케줄이 있으면.
        //       if (drs.am_schedule.length() > idx && drs.am_schedule.charAt(idx) != '-') schedule[idx     ] = (byte)drs.am_usechair;
        //     };

        //     if (drs.pm_bookyn == 'Y' && drs.pm_usechair > 0) for(idx = 0; idx < 84; ++idx) {
        //       // 오후 스케줄이 있으면.
        //       if (drs.pm_schedule.length() > idx && drs.pm_schedule.charAt(idx) != '-') schedule[idx + 72] = (byte)drs.pm_usechair;
        //     };

        //     while(date.isAfter(rsv.rsvdate.toLocalDate())) rsv = itRsv.next();
        //     while(date.compareTo(rsv.rsvdate.toLocalDate()) == 0) {
        //       LocalTime lt = rsv.rsvdate.toLocalTime();
        //       int loc = ((lt.getHour() - 7) * 60 + lt.getMinute()) / 5;
        //       int len = rsv.tx_time / 5;

        //       while (len > 0) {
        //         if (schedule[loc + len] > 0) {
        //           // 중복 예약 불가시 예약 가능을 0으로 고정
        //           if (rsv.overlap != 'Y' /* || e.overlap != 'Y'*/) schedule[loc + len] = 0;
        //           else {
        //             schedule[loc + len]--;
        //           }
        //         }
        //         len--;
        //       }
        //       rsv = itRsv.next();
        //     }

        //     int len = e.tx_time / 5;
        //     int sIdx = -1, eIdx = -1;
        //     for(idx = 0; idx < 157;) {
        //       if (schedule[idx] > 0) {
        //         if (sIdx < 0) sIdx = idx;
        //       } else
        //       if (sIdx >= 0 && schedule[idx] == 0) {
        //         eIdx = idx - len;
        //         if (sIdx <= eIdx) {
        //           if (idx < 72) {     // 오전
        //             days[dIdx] |= 1;
        //             idx = 71;
        //           } else {            // 오후
        //             days[dIdx] |= 2;
        //             break;
        //           }
        //         }
        //         sIdx = -1; eIdx = -1;
        //       }
        //       idx++;
        //     }
        //   }

        //   while(!date.isBefore(hol)) hol = itHols.next();
        //   while(!date.isBefore(drs.medddate)) drs = itDrs.next();
        //   ++dIdx;
        // }
        //Arrays.stream(days).forEach
        //days.forEach(System.out::println);
        System.out.println(String.format("dr : %s, dept: %s (%s) - (%s) %d",
                e.rsvdr, e.meddept,
                ym.format(DateTimeFormatter.ofPattern("yyyy.MM")),
                drs.medddate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), drschedule.size()));
        System.out.println(Arrays.toString(days));
        System.out.println("====================================================================================");
        //break;
      }
    };
  }
}
