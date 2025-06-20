package org.example.greeting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {
    @Query(value = "SELECT * FROM greeting g WHERE g.deleted_at IS NOT NULL", nativeQuery = true)
    List<Greeting> findAllByDeletedAtIsNotNull();
}
