package com.microservices.serviceone.repository;

import com.microservices.serviceone.domain.Brigade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Brigade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrigadeRepository extends JpaRepository<Brigade, Long> {
    List<Brigade> findByGovernorate_Id(Long governorateId);
}
