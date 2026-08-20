package com.example.electrostorage.dto;

public class CreateOrderRequest {

    private Long supplierId;

    public CreateOrderRequest() {
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
