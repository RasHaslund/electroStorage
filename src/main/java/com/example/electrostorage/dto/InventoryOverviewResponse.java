package com.example.electrostorage.dto;

public class InventoryOverviewResponse {

    private Long componentId;
    private String description;
    private Integer totalReceivedQuantity;

    public InventoryOverviewResponse() {
    }

    public InventoryOverviewResponse(Long componentId, String description, Integer totalReceivedQuantity) {
        this.componentId = componentId;
        this.description = description;
        this.totalReceivedQuantity = totalReceivedQuantity;
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

    public Integer getTotalReceivedQuantity() {
        return totalReceivedQuantity;
    }

    public void setTotalReceivedQuantity(Integer totalReceivedQuantity) {
        this.totalReceivedQuantity = totalReceivedQuantity;
    }
}
