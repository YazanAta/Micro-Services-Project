package com.microservices.serviceone.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.microservices.serviceone.domain.Governorate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GovernorateDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double area;

    private Integer population;

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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GovernorateDTO)) {
            return false;
        }

        GovernorateDTO governorateDTO = (GovernorateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, governorateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GovernorateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", area=" + getArea() +
            ", population=" + getPopulation() +
            "}";
    }
}
