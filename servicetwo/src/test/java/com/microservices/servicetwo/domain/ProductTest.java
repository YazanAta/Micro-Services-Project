package com.microservices.servicetwo.domain;

import static com.microservices.servicetwo.domain.CategoryTestSamples.*;
import static com.microservices.servicetwo.domain.OrderTestSamples.*;
import static com.microservices.servicetwo.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservices.servicetwo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoryTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void orderTest() {
        Product product = getProductRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        product.addOrder(orderBack);
        assertThat(product.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProducts()).containsOnly(product);

        product.removeOrder(orderBack);
        assertThat(product.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProducts()).doesNotContain(product);

        product.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(product.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProducts()).containsOnly(product);

        product.setOrders(new HashSet<>());
        assertThat(product.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProducts()).doesNotContain(product);
    }
}
