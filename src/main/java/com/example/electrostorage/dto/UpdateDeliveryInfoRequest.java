package com.example.electrostorage.dto;

import java.time.LocalDate;

public class UpdateDeliveryInfoRequest {

    private String trackingCode;
    private LocalDate expectedDeliveryDate;

    public UpdateDeliveryInfoRequest() {
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
}
