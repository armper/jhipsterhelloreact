package com.service.hello.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cycle.
 */
@Entity
@Table(name = "cycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "volume")
    private Integer volume;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cycles", "routine" }, allowSetters = true)
    private Excercise excercise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReps() {
        return this.reps;
    }

    public Cycle reps(Integer reps) {
        this.setReps(reps);
        return this;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getVolume() {
        return this.volume;
    }

    public Cycle volume(Integer volume) {
        this.setVolume(volume);
        return this;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Excercise getExcercise() {
        return this.excercise;
    }

    public void setExcercise(Excercise excercise) {
        this.excercise = excercise;
    }

    public Cycle excercise(Excercise excercise) {
        this.setExcercise(excercise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cycle)) {
            return false;
        }
        return id != null && id.equals(((Cycle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cycle{" +
            "id=" + getId() +
            ", reps=" + getReps() +
            ", volume=" + getVolume() +
            "}";
    }
}
