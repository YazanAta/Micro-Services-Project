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
 * A Brigade.
 */
@Entity
@Table(name = "brigade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Brigade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "established_year")
    private Integer establishedYear;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brigade", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "brigade" }, allowSetters = true)
    private Set<Municipality> municipalities = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "brigade" }, allowSetters = true)
    private Governorate governorate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Brigade id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Brigade name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public Brigade type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEstablishedYear() {
        return this.establishedYear;
    }

    public Brigade establishedYear(Integer establishedYear) {
        this.setEstablishedYear(establishedYear);
        return this;
    }

    public void setEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }

    public Set<Municipality> getMunicipalities() {
        return this.municipalities;
    }

    public void setMunicipalities(Set<Municipality> municipalities) {
        if (this.municipalities != null) {
            this.municipalities.forEach(i -> i.setBrigades(null));
        }
        if (municipalities != null) {
            municipalities.forEach(i -> i.setBrigades(this));
        }
        this.municipalities = municipalities;
    }

    public Brigade municipalities(Set<Municipality> municipalities) {
        this.setMunicipalities(municipalities);
        return this;
    }

    public Brigade addMunicipality(Municipality municipality) {
        this.municipalities.add(municipality);
        municipality.setBrigades(this);
        return this;
    }

    public Brigade removeMunicipality(Municipality municipality) {
        this.municipalities.remove(municipality);
        municipality.setBrigades(null);
        return this;
    }

    public Governorate getGovernorate() {
        return this.governorate;
    }

    public void setGovernorate(Governorate governorate) {
        this.governorate = governorate;
    }

    public Brigade governorate(Governorate governorate) {
        this.setGovernorate(governorate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Brigade)) {
            return false;
        }
        return getId() != null && getId().equals(((Brigade) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Brigade{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", establishedYear=" + getEstablishedYear() +
            "}";
    }
}
