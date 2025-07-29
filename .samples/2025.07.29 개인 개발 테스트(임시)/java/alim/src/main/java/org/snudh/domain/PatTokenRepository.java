package org.snudh.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PatTokenRepository extends JpaRepository<PatToken, Long> {

  public PatToken findByToken(String token);

  @Query("SELECT m FROM PatToken m\n"
       + "WHERE m.patno = :patno\n"
       + "  AND m.rsvdate = :rsvdate\n"
       + "  AND m.meddept = :meddept")
  public PatToken getTockenByRsvInfo(
     @Param("patno"    )String patno,
     @Param("rsvdate"  )LocalDateTime rsvdate,
     @Param("meddept"  )String meddept);
}
