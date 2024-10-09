package com.microservices.serviceone.repository;

import com.microservices.serviceone.domain.Brigade;
import com.microservices.serviceone.domain.Municipality;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Municipality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    List<Municipality> findByBrigade_Id(Long municipalityId);
}
