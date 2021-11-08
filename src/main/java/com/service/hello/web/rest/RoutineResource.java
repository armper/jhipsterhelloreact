package com.service.hello.web.rest;

import com.service.hello.domain.Routine;
import com.service.hello.repository.RoutineRepository;
import com.service.hello.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.service.hello.domain.Routine}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RoutineResource {

    private final Logger log = LoggerFactory.getLogger(RoutineResource.class);

    private static final String ENTITY_NAME = "routine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoutineRepository routineRepository;

    public RoutineResource(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    /**
     * {@code POST  /routines} : Create a new routine.
     *
     * @param routine the routine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new routine, or with status {@code 400 (Bad Request)} if the routine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/routines")
    public ResponseEntity<Routine> createRoutine(@RequestBody Routine routine) throws URISyntaxException {
        log.debug("REST request to save Routine : {}", routine);
        if (routine.getId() != null) {
            throw new BadRequestAlertException("A new routine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Routine result = routineRepository.save(routine);
        return ResponseEntity
            .created(new URI("/api/routines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /routines/:id} : Updates an existing routine.
     *
     * @param id the id of the routine to save.
     * @param routine the routine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routine,
     * or with status {@code 400 (Bad Request)} if the routine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the routine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/routines/{id}")
    public ResponseEntity<Routine> updateRoutine(@PathVariable(value = "id", required = false) final Long id, @RequestBody Routine routine)
        throws URISyntaxException {
        log.debug("REST request to update Routine : {}, {}", id, routine);
        if (routine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Routine result = routineRepository.save(routine);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, routine.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /routines/:id} : Partial updates given fields of an existing routine, field will ignore if it is null
     *
     * @param id the id of the routine to save.
     * @param routine the routine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routine,
     * or with status {@code 400 (Bad Request)} if the routine is not valid,
     * or with status {@code 404 (Not Found)} if the routine is not found,
     * or with status {@code 500 (Internal Server Error)} if the routine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/routines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Routine> partialUpdateRoutine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Routine routine
    ) throws URISyntaxException {
        log.debug("REST request to partial update Routine partially : {}, {}", id, routine);
        if (routine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Routine> result = routineRepository
            .findById(routine.getId())
            .map(existingRoutine -> {
                if (routine.getName() != null) {
                    existingRoutine.setName(routine.getName());
                }
                if (routine.getDateStarted() != null) {
                    existingRoutine.setDateStarted(routine.getDateStarted());
                }
                if (routine.getDateEnded() != null) {
                    existingRoutine.setDateEnded(routine.getDateEnded());
                }
                if (routine.getGoalDate() != null) {
                    existingRoutine.setGoalDate(routine.getGoalDate());
                }
                if (routine.getStartingBodyWeight() != null) {
                    existingRoutine.setStartingBodyWeight(routine.getStartingBodyWeight());
                }
                if (routine.getEndingBodyWeight() != null) {
                    existingRoutine.setEndingBodyWeight(routine.getEndingBodyWeight());
                }

                return existingRoutine;
            })
            .map(routineRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, routine.getId().toString())
        );
    }

    /**
     * {@code GET  /routines} : get all the routines.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of routines in body.
     */
    @GetMapping("/routines")
    public List<Routine> getAllRoutines(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Routines");
        return routineRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /routines/:id} : get the "id" routine.
     *
     * @param id the id of the routine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the routine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/routines/{id}")
    public ResponseEntity<Routine> getRoutine(@PathVariable Long id) {
        log.debug("REST request to get Routine : {}", id);
        Optional<Routine> routine = routineRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(routine);
    }

    /**
     * {@code DELETE  /routines/:id} : delete the "id" routine.
     *
     * @param id the id of the routine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/routines/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        log.debug("REST request to delete Routine : {}", id);
        routineRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
