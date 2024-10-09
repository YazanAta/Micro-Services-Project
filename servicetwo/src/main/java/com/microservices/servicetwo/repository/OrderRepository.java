package com.microservices.servicetwo.repository;

import com.microservices.servicetwo.domain.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 *
 * When extending this class, extend OrderRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface OrderRepository extends OrderRepositoryWithBagRelationships, JpaRepository<Order, Long> {
    default Optional<Order> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Order> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Order> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select jhiOrder from Order jhiOrder left join fetch jhiOrder.customer",
        countQuery = "select count(jhiOrder) from Order jhiOrder"
    )
    Page<Order> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jhiOrder from Order jhiOrder left join fetch jhiOrder.customer")
    List<Order> findAllWithToOneRelationships();

    @Query("select jhiOrder from Order jhiOrder left join fetch jhiOrder.customer where jhiOrder.id =:id")
    Optional<Order> findOneWithToOneRelationships(@Param("id") Long id);
}
