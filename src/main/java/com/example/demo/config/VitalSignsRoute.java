package com.example.demo.config;



import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.example.demo.config.entity.AverageVitalSigns;
import com.example.demo.config.proccessor.CustomAggregator;
import com.example.demo.entity.VitalSigns;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VitalSignsRoute extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        
        from("kafka:vital-signs-topic?brokers=localhost:9092")
            .log("Received message from Kafka topic vital-signs-topic: ${body}")
            .process(exchange -> {
                String message = exchange.getIn().getBody(String.class);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    JsonNode transactionJson = mapper.readTree(message);
                    String patientId = transactionJson.get("patientId").asText();
                    double heartRate = transactionJson.get("heartRate").asDouble();
                    double bloodPressure = transactionJson.get("bloodPressure").asDouble();
                    VitalSigns vitalSigns = new VitalSigns(patientId, heartRate, bloodPressure);
                    exchange.getIn().setBody(vitalSigns);
                    exchange.getIn().setHeader("patientId", patientId);
                    exchange.getIn().setHeader("time", transactionJson.get("time").asLong());
                } catch (JsonProcessingException e) {
                    // Handle the exception
                }
                // Log the message after processing
                log.info("Processed message: ${body}");
            })
            .log("Aggregating messages for patient: ${header.patientId}")
            .aggregate(header("patientId"), new CustomAggregator())
            .completionTimeout(5000)
            .process(exchange -> {
                String patientId = exchange.getIn().getHeader("patientId", String.class);
                Double avgHeartRate = exchange.getProperty("avgHeartRate", Double.class);
                Double avgBloodPressure = exchange.getProperty("avgBloodPressure", Double.class);
                AverageVitalSigns averageVitalSigns = new AverageVitalSigns(patientId, avgHeartRate, avgBloodPressure);
                exchange.getIn().setBody(averageVitalSigns);
                // Log the message after processing
                log.info("Processed message: ${body}");
            })
            .log("Marshalling message to JSON")
            .marshal().json(JsonLibrary.Jackson)
            .log("Sending message with body: ${body} to Kafka topic average-vital-signs-topic-per-minute")
            .to("kafka:average-vital-signs-topic?brokers=localhost:9092");
    }
}




