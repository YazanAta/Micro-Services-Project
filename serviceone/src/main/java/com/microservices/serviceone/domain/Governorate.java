package com.microservices.serviceone.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Governorate.
 */
@Entity
@Table(name = "governorate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Governorate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "area")
    private Double area;

    @Column(name = "population")
    private Integer population;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "governorate", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "municipalities", "governorate" }, allowSetters = true)
    private Set<Brigade> brigades = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Governorate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Governorate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getArea() {
        return this.area;
    }

    public Governorate area(Double area) {
        this.setArea(area);
        return this;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getPopulation() {
        return this.population;
    }

    public Governorate population(Integer population) {
        this.setPopulation(population);
        return this;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Set<Brigade> getBrigades() {
        return this.brigades;
    }

    public void setBrigades(Set<Brigade> brigades) {
        if (this.brigades != null) {
            this.brigades.forEach(i -> i.setGovernorate(null));
        }
        if (brigades != null) {
            brigades.forEach(i -> i.setGovernorate(this));
        }
        this.brigades = brigades;
    }

    public Governorate brigades(Set<Brigade> brigades) {
        this.setBrigades(brigades);
        return this;
    }

    public Governorate addBrigades(Brigade brigade) {
        this.brigades.add(brigade);
        brigade.setGovernorate(this);
        return this;
    }

    public Governorate removeBrigades(Brigade brigade) {
        this.brigades.remove(brigade);
        brigade.setGovernorate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Governorate)) {
            return false;
        }
        return getId() != null && getId().equals(((Governorate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Governorate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", area=" + getArea() +
            ", population=" + getPopulation() +
            "}";
    }
}
