package com.example.demo.config.entity;

public class AverageVitalSigns {
    private String patientId;
    private Double avgHeartRate;
    private Double avgBloodPressure;

    public AverageVitalSigns(String patientId, Double avgHeartRate, Double avgBloodPressure) {
        this.patientId = patientId;
        this.avgHeartRate = avgHeartRate;
        this.avgBloodPressure = avgBloodPressure;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Double getAvgHeartRate() {
        return avgHeartRate;
    }

    public void setAvgHeartRate(Double avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
    }

    public Double getAvgBloodPressure() {
        return avgBloodPressure;
    }

    public void setAvgBloodPressure(Double avgBloodPressure) {
        this.avgBloodPressure = avgBloodPressure;
    }
}

