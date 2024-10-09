package com.microservices.servicetwo.service;

import com.microservices.servicetwo.domain.Payment;
import com.microservices.servicetwo.repository.PaymentRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.microservices.servicetwo.domain.Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment save(Payment payment) {
        LOG.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Update a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment update(Payment payment) {
        LOG.debug("Request to update Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Partially update a payment.
     *
     * @param payment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Payment> partialUpdate(Payment payment) {
        LOG.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(existingPayment -> {
                if (payment.getPaymentDate() != null) {
                    existingPayment.setPaymentDate(payment.getPaymentDate());
                }
                if (payment.getAmount() != null) {
                    existingPayment.setAmount(payment.getAmount());
                }
                if (payment.getPaymentMethod() != null) {
                    existingPayment.setPaymentMethod(payment.getPaymentMethod());
                }

                return existingPayment;
            })
            .map(paymentRepository::save);
    }

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        LOG.debug("Request to get all Payments");
        return paymentRepository.findAll();
    }

    /**
     *  Get all the payments where Order is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Payment> findAllWhereOrderIsNull() {
        LOG.debug("Request to get all payments where Order is null");
        return StreamSupport.stream(paymentRepository.findAll().spliterator(), false)
            .filter(payment -> payment.getOrder() == null)
            .toList();
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        LOG.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
