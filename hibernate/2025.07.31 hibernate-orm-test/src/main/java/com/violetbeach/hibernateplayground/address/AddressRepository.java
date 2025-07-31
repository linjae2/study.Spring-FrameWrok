package com.violetbeach.hibernateplayground.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository <T extends Address> extends JpaRepository<T, Long> {
    List<T> findAll();
}
