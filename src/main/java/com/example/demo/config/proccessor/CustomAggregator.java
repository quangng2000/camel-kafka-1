package com.example.demo.config.proccessor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import com.example.demo.entity.VitalSigns;

public class CustomAggregator implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            // First message for this patient
            List<Double> heartRates = new ArrayList<>();
            List<Double> bloodPressures = new ArrayList<>();
            heartRates.add(newExchange.getIn().getBody(VitalSigns.class).getHeartRate());
            bloodPressures.add(newExchange.getIn().getBody(VitalSigns.class).getBloodPressure());
            newExchange.setProperty("heartRates", heartRates);
            newExchange.setProperty("bloodPressures", bloodPressures);
            return newExchange;
        } else {
            // Aggregate values for this patient
            List<Double> heartRates = oldExchange.getProperty("heartRates", List.class);
            List<Double> bloodPressures = oldExchange.getProperty("bloodPressures", List.class);
            heartRates.add(newExchange.getIn().getBody(VitalSigns.class).getHeartRate());
            bloodPressures.add(newExchange.getIn().getBody(VitalSigns.class).getBloodPressure());
            oldExchange.setProperty("heartRates", heartRates);
            oldExchange.setProperty("bloodPressures", bloodPressures);
            double avgHeartRate = heartRates.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double avgBloodPressure = bloodPressures.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            oldExchange.setProperty("avgHeartRate", avgHeartRate);
            oldExchange.setProperty("avgBloodPressure", avgBloodPressure);
            return oldExchange;
        }
    }
}


