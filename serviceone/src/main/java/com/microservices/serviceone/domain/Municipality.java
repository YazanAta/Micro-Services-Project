package com.microservices.serviceone.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Municipality.
 */
@Entity
@Table(name = "municipality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Municipality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "municipalities", "governorate" }, allowSetters = true)
    private Brigade brigade;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Municipality id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Municipality name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brigade getBrigades() {
        return this.brigade;
    }

    public void setBrigades(Brigade brigade) {
        this.brigade = brigade;
    }

    public Municipality brigades(Brigade brigade) {
        this.setBrigades(brigade);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Municipality)) {
            return false;
        }
        return getId() != null && getId().equals(((Municipality) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Municipality{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
