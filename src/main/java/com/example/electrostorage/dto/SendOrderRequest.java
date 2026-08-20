package com.example.electrostorage.dto;

import java.time.LocalDate;

public class SendOrderRequest {

    private LocalDate expectedDeliveryDate;

    public SendOrderRequest() {
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
}
