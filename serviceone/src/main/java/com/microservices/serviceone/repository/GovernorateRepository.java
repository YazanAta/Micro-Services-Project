package com.microservices.serviceone.repository;

import com.microservices.serviceone.domain.Governorate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Governorate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GovernorateRepository extends JpaRepository<Governorate, Long> {}
