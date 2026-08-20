package com.example.electrostorage.dto;

public class AssemblyItemResponse {

    private Long componentId;
    private String description;
    private Integer quantity;

    public AssemblyItemResponse() {
    }

    public AssemblyItemResponse(Long componentId, String description, Integer quantity) {
        this.componentId = componentId;
        this.description = description;
        this.quantity = quantity;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
