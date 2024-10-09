package com.microservices.servicetwo;

import com.microservices.servicetwo.config.AsyncSyncConfiguration;
import com.microservices.servicetwo.config.EmbeddedSQL;
import com.microservices.servicetwo.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { ServicetwoApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
