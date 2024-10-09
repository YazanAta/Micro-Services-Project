package com.microservices.servicetwo.domain;

import static com.microservices.servicetwo.domain.OrderTestSamples.*;
import static com.microservices.servicetwo.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.servicetwo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shipment.class);
        Shipment shipment1 = getShipmentSample1();
        Shipment shipment2 = new Shipment();
        assertThat(shipment1).isNotEqualTo(shipment2);

        shipment2.setId(shipment1.getId());
        assertThat(shipment1).isEqualTo(shipment2);

        shipment2 = getShipmentSample2();
        assertThat(shipment1).isNotEqualTo(shipment2);
    }

    @Test
    void orderTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        shipment.setOrder(orderBack);
        assertThat(shipment.getOrder()).isEqualTo(orderBack);
        assertThat(orderBack.getShipment()).isEqualTo(shipment);

        shipment.order(null);
        assertThat(shipment.getOrder()).isNull();
        assertThat(orderBack.getShipment()).isNull();
    }
}
