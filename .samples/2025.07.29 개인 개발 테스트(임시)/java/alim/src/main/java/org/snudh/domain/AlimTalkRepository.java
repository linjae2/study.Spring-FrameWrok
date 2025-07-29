package org.snudh.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlimTalkRepository extends JpaRepository<AlimTalk, Long> {

  @Query("SELECT exists(SELECT 1 FROM AlimTalk m\n"
       + "WHERE m.reg_dt >= (CURRENT_DATE - 2 DAY)\n"
       + "  AND m.status > 0)")
  public boolean existsPopedData();

  @Query("SELECT m FROM AlimTalk m\n"
       + "WHERE m.reg_dt >= (CURRENT_DATE - 2 DAY)\n"
       + "  AND m.status > 0\n"
       + "ORDER BY m.reg_dt")
  public List<AlimTalk> getPopData();
}
