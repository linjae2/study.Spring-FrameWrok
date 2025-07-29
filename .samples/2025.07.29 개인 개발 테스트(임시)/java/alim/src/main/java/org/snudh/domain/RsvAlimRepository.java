package org.snudh.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RsvAlimRepository extends JpaRepository<RsvAlim, RsvAlim.Key> {

  @Query("SELECT exists(SELECT 1 FROM RsvAlim m)")
  public boolean existsRsvAlim1();

  @Query("SELECT exists(SELECT 1 FROM RsvAlim m\n"
       + " WHERE m.key.rsvDate = :rsvDate\n"
       + "   AND m.key.dDay = :dDay\n"
       + "   AND m.key.msgType = :msgType\n"
       + "   AND m.patno = :patno\n"
       + "   AND m.meddate = :meddate\n"
       + "   AND m.meddept = :meddept)")
  public boolean existsRsvAlim(
    @Param("rsvDate"  )LocalDate     rsvDate,
    @Param("dDay"     )Integer       dDay,
    @Param("msgType"  )String        msgType,
    @Param("patno"    )String        patno,
    @Param("meddate"  )LocalDateTime meddate,
    @Param("meddept"  )String        meddept
  );

  @Query("SELECT m FROM RsvAlim m\n"
       + " WHERE m.key.rsvDate = :rsvDate\n"
       + "   AND m.key.dDay = :dDay\n"
       + "   AND m.key.msgType = :msgType\n"
       + "   AND m.patno = :patno\n"
       + "   AND m.meddate = :meddate\n"
       + "   AND m.meddept = :meddept")
  public RsvAlim getRsvAlim(
    @Param("rsvDate"  )LocalDate     rsvDate,
    @Param("dDay"     )Integer       dDay,
    @Param("msgType"  )String        msgType,
    @Param("patno"    )String        patno,
    @Param("meddate"  )LocalDateTime meddate,
    @Param("meddept"  )String        meddept
  );

  @Query("SELECT max(m.key.seq) FROM RsvAlim m\n"
       + "WHERE m.key.rsvDate = :rsvDate\n"
       + "  AND m.key.dDay = :dDay\n"
       + "  AND m.key.msgType = :msgType")
  public Integer getMaxSequence(
    @Param("rsvDate"  )LocalDate     rsvDate,
    @Param("dDay"     )Integer       dDay,
    @Param("msgType"  )String        msgType
  );

}
