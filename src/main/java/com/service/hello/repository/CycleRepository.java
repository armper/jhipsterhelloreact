package com.service.hello.repository;

import com.service.hello.domain.Cycle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {}
