package com.example.electrostorage.dto;

public class CreateAssemblyItemRequest {

    private Long componentId;
    private Integer quantity;

    public CreateAssemblyItemRequest() {
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
