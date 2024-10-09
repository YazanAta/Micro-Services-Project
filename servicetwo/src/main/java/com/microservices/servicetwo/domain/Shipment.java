package com.microservices.servicetwo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microservices.servicetwo.domain.enumeration.ShipmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Shipment.
 */
@Entity
@Table(name = "shipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Shipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "shipment_date", nullable = false)
    private Instant shipmentDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status", nullable = false)
    private ShipmentStatus shipmentStatus;

    @JsonIgnoreProperties(value = { "payment", "shipment", "orderItems", "customer", "products" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "shipment")
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shipment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getShipmentDate() {
        return this.shipmentDate;
    }

    public Shipment shipmentDate(Instant shipmentDate) {
        this.setShipmentDate(shipmentDate);
        return this;
    }

    public void setShipmentDate(Instant shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public Shipment trackingNumber(String trackingNumber) {
        this.setTrackingNumber(trackingNumber);
        return this;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public ShipmentStatus getShipmentStatus() {
        return this.shipmentStatus;
    }

    public Shipment shipmentStatus(ShipmentStatus shipmentStatus) {
        this.setShipmentStatus(shipmentStatus);
        return this;
    }

    public void setShipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.setShipment(null);
        }
        if (order != null) {
            order.setShipment(this);
        }
        this.order = order;
    }

    public Shipment order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shipment)) {
            return false;
        }
        return getId() != null && getId().equals(((Shipment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shipment{" +
            "id=" + getId() +
            ", shipmentDate='" + getShipmentDate() + "'" +
            ", trackingNumber='" + getTrackingNumber() + "'" +
            ", shipmentStatus='" + getShipmentStatus() + "'" +
            "}";
    }
}
