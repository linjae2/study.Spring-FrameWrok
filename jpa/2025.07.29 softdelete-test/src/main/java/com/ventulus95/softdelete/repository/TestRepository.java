package com.ventulus95.softdelete.repository;

import com.ventulus95.softdelete.domain.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<MemberShip, Long> {
}
