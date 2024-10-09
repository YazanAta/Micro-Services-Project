package com.microservices.serviceone.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.microservices.serviceone.domain.Municipality} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MunicipalityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BrigadeDTO brigade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrigadeDTO getBrigade() {
        return brigade;
    }

    public void setBrigade(BrigadeDTO brigade) {
        this.brigade = brigade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MunicipalityDTO)) {
            return false;
        }

        MunicipalityDTO municipalityDTO = (MunicipalityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, municipalityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MunicipalityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", brigades=" + getBrigade() +
            "}";
    }
}
