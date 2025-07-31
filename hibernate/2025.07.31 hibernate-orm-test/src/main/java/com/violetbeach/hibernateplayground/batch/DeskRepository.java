package com.violetbeach.hibernateplayground.batch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeskRepository extends JpaRepository<Desk, Long> {

    List<Desk> findAll();

}
