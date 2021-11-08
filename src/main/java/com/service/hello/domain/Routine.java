package com.service.hello.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Routine.
 */
@Entity
@Table(name = "routine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Routine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_started")
    private LocalDate dateStarted;

    @Column(name = "date_ended")
    private LocalDate dateEnded;

    @Column(name = "goal_date")
    private LocalDate goalDate;

    @Column(name = "starting_body_weight")
    private Integer startingBodyWeight;

    @Column(name = "ending_body_weight")
    private Integer endingBodyWeight;

    @OneToMany(mappedBy = "routine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycles", "routine" }, allowSetters = true)
    private Set<Excercise> excercises = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_routine__user",
        joinColumns = @JoinColumn(name = "routine_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Routine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Routine name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateStarted() {
        return this.dateStarted;
    }

    public Routine dateStarted(LocalDate dateStarted) {
        this.setDateStarted(dateStarted);
        return this;
    }

    public void setDateStarted(LocalDate dateStarted) {
        this.dateStarted = dateStarted;
    }

    public LocalDate getDateEnded() {
        return this.dateEnded;
    }

    public Routine dateEnded(LocalDate dateEnded) {
        this.setDateEnded(dateEnded);
        return this;
    }

    public void setDateEnded(LocalDate dateEnded) {
        this.dateEnded = dateEnded;
    }

    public LocalDate getGoalDate() {
        return this.goalDate;
    }

    public Routine goalDate(LocalDate goalDate) {
        this.setGoalDate(goalDate);
        return this;
    }

    public void setGoalDate(LocalDate goalDate) {
        this.goalDate = goalDate;
    }

    public Integer getStartingBodyWeight() {
        return this.startingBodyWeight;
    }

    public Routine startingBodyWeight(Integer startingBodyWeight) {
        this.setStartingBodyWeight(startingBodyWeight);
        return this;
    }

    public void setStartingBodyWeight(Integer startingBodyWeight) {
        this.startingBodyWeight = startingBodyWeight;
    }

    public Integer getEndingBodyWeight() {
        return this.endingBodyWeight;
    }

    public Routine endingBodyWeight(Integer endingBodyWeight) {
        this.setEndingBodyWeight(endingBodyWeight);
        return this;
    }

    public void setEndingBodyWeight(Integer endingBodyWeight) {
        this.endingBodyWeight = endingBodyWeight;
    }

    public Set<Excercise> getExcercises() {
        return this.excercises;
    }

    public void setExcercises(Set<Excercise> excercises) {
        if (this.excercises != null) {
            this.excercises.forEach(i -> i.setRoutine(null));
        }
        if (excercises != null) {
            excercises.forEach(i -> i.setRoutine(this));
        }
        this.excercises = excercises;
    }

    public Routine excercises(Set<Excercise> excercises) {
        this.setExcercises(excercises);
        return this;
    }

    public Routine addExcercise(Excercise excercise) {
        this.excercises.add(excercise);
        excercise.setRoutine(this);
        return this;
    }

    public Routine removeExcercise(Excercise excercise) {
        this.excercises.remove(excercise);
        excercise.setRoutine(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Routine users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Routine addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Routine removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Routine)) {
            return false;
        }
        return id != null && id.equals(((Routine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Routine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateStarted='" + getDateStarted() + "'" +
            ", dateEnded='" + getDateEnded() + "'" +
            ", goalDate='" + getGoalDate() + "'" +
            ", startingBodyWeight=" + getStartingBodyWeight() +
            ", endingBodyWeight=" + getEndingBodyWeight() +
            "}";
    }
}
