package com.microservices.serviceone.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.microservices.serviceone.domain.Brigade} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BrigadeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String type;

    private Integer establishedYear;

    @NotNull
    private GovernorateDTO governorate;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEstablishedYear() {
        return establishedYear;
    }

    public void setEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }

    public GovernorateDTO getGovernorate() {
        return governorate;
    }

    public void setGovernorate(GovernorateDTO governorate) {
        this.governorate = governorate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrigadeDTO)) {
            return false;
        }

        BrigadeDTO brigadeDTO = (BrigadeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, brigadeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BrigadeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", establishedYear=" + getEstablishedYear() +
            ", governorate=" + getGovernorate() +
            "}";
    }
}
