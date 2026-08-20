package com.example.electrostorage.dto;

public class AddOrderLineRequest {

    private Long componentId;
    private Integer quantity;

    public AddOrderLineRequest() {
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
