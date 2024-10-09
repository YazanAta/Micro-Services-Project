package com.microservices.servicetwo.domain;

import static com.microservices.servicetwo.domain.CustomerTestSamples.*;
import static com.microservices.servicetwo.domain.OrderItemTestSamples.*;
import static com.microservices.servicetwo.domain.OrderTestSamples.*;
import static com.microservices.servicetwo.domain.PaymentTestSamples.*;
import static com.microservices.servicetwo.domain.ProductTestSamples.*;
import static com.microservices.servicetwo.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.servicetwo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void paymentTest() {
        Order order = getOrderRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        order.setPayment(paymentBack);
        assertThat(order.getPayment()).isEqualTo(paymentBack);

        order.payment(null);
        assertThat(order.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        Order order = getOrderRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        order.setShipment(shipmentBack);
        assertThat(order.getShipment()).isEqualTo(shipmentBack);

        order.shipment(null);
        assertThat(order.getShipment()).isNull();
    }

    @Test
    void orderItemTest() {
        Order order = getOrderRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        order.addOrderItem(orderItemBack);
        assertThat(order.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(order);

        order.removeOrderItem(orderItemBack);
        assertThat(order.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();

        order.orderItems(new HashSet<>(Set.of(orderItemBack)));
        assertThat(order.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(order);

        order.setOrderItems(new HashSet<>());
        assertThat(order.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();
    }

    @Test
    void customerTest() {
        Order order = getOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        order.setCustomer(customerBack);
        assertThat(order.getCustomer()).isEqualTo(customerBack);

        order.customer(null);
        assertThat(order.getCustomer()).isNull();
    }

    @Test
    void productTest() {
        Order order = getOrderRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        order.addProduct(productBack);
        assertThat(order.getProducts()).containsOnly(productBack);

        order.removeProduct(productBack);
        assertThat(order.getProducts()).doesNotContain(productBack);

        order.products(new HashSet<>(Set.of(productBack)));
        assertThat(order.getProducts()).containsOnly(productBack);

        order.setProducts(new HashSet<>());
        assertThat(order.getProducts()).doesNotContain(productBack);
    }
}
