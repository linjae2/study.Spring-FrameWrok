package org.snudh.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedAlimRepository extends JpaRepository<SchedAlim, SchedAlim.Key> {

  // WHERE m.alim.execDay >= DATEADD('DAY', -2, CURRENT_DATE())
  // @Query("SELECT exists(SELECT 1 FROM SchedAlim m\n"
  //      + "WHERE m.alim.execDay >= SUBDATE(CURRENT_DATE(), 'INTERVAL 2 DAY'))")
  //@Query("SELECT exists(SELECT 1 FROM SchedAlim m WHERE m.alim.execDay >= CURRENT_DATE())")
  // @Query("SELECT exists(SELECT 1 FROM SchedAlim m\n"
  //      + "WHERE m.alim.execDay >= FUNCTION('DATEADD', 'DAY', -2, CURRENT_DATE()))")
  @Query("SELECT exists(SELECT 1 FROM SchedAlim m\n"
       + "WHERE m.key.execDay >= (CURRENT_DATE - 2 DAY)\n"
       + "  AND m.msg_st > 0)")
  public boolean existsPopedData();

  @Query("SELECT m FROM SchedAlim m\n"
       + "WHERE m.key.execDay >= (CURRENT_DATE - 2 DAY)\n"
       + "  AND m.msg_st > 0\n"
       + "ORDER BY m.cdate limit 1")
  public SchedAlim topData();
}
