package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VitalSigns {
    private String patientId;
    private double heartRate;
    private double bloodPressure;
    private long time;

    @JsonCreator
    public VitalSigns(@JsonProperty("patientId") String patientId,
                      @JsonProperty("heartRate") double heartRate,
                      @JsonProperty("bloodPressure") double bloodPressure) {
        this.patientId = patientId;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}




