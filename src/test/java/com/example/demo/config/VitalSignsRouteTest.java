package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

@Component
public class VitalSignsRouteTest extends CamelTestSupport {

    @Produce("direct:start")
    private ProducerTemplate producer;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Override Kafka endpoint with a mock
        RouteDefinition route = context.getRouteDefinition("VitalSignsRoute");
        RouteBuilder newRoute = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .routeId("VitalSignsRouteTest")
                        .to("mock:kafka");
            }
        };
        context.addRoutes(newRoute);

        // Replace the original route with the new one
        context.getRouteDefinitions().remove(route);
        context.addRouteDefinitions(newRoute.getRouteCollection().getRoutes());
    }


    @EndpointInject("mock:kafka")
    private MockEndpoint mockKafka;

    @Test
    void given_validInputMessage_when_processed_then_transformedAsExpected() throws InterruptedException {
        // Given
        String inputMessage = "{\"patientId\":\"p001\",\"heartRate\":70.0,\"bloodPressure\":120.0,\"time\":1621535932056}";
        String expectedOutputMessage = "{\"patientId\":\"p001\",\"avgHeartRate\":70.0,\"avgBloodPressure\":120.0}";

        // When
        producer.sendBodyAndHeader(inputMessage, "p001", KafkaConstants.KEY);

        // Then
        assertTrue(mockKafka.getExchanges().size() > 0, "Output message not produced to Kafka");
        TimeUnit.SECONDS.sleep(1); // Wait for the output message to be processed by the route
        String outputMessage = consumer.receiveBody("mock:kafka", String.class);
        assertEquals(expectedOutputMessage, outputMessage, "Output message not transformed as expected");
    }
}





