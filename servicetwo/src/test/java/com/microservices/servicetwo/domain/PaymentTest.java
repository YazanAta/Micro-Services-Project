package com.microservices.servicetwo.domain;

import static com.microservices.servicetwo.domain.OrderTestSamples.*;
import static com.microservices.servicetwo.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.servicetwo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void orderTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        payment.setOrder(orderBack);
        assertThat(payment.getOrder()).isEqualTo(orderBack);
        assertThat(orderBack.getPayment()).isEqualTo(payment);

        payment.order(null);
        assertThat(payment.getOrder()).isNull();
        assertThat(orderBack.getPayment()).isNull();
    }
}
