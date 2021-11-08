package com.service.hello.repository;

import com.service.hello.domain.Routine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Routine entity.
 */
@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    @Query(
        value = "select distinct routine from Routine routine left join fetch routine.users",
        countQuery = "select count(distinct routine) from Routine routine"
    )
    Page<Routine> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct routine from Routine routine left join fetch routine.users")
    List<Routine> findAllWithEagerRelationships();

    @Query("select routine from Routine routine left join fetch routine.users where routine.id =:id")
    Optional<Routine> findOneWithEagerRelationships(@Param("id") Long id);
}
