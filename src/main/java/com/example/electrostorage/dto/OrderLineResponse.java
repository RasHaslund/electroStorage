package com.example.electrostorage.dto;

public class OrderLineResponse {

    private Long id;
    private Long componentId;
    private String componentDescription;
    private Integer quantity;

    public OrderLineResponse() {
    }

    public OrderLineResponse(Long id, Long componentId, String componentDescription, Integer quantity) {
        this.id = id;
        this.componentId = componentId;
        this.componentDescription = componentDescription;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getComponentDescription() {
        return componentDescription;
    }

    public void setComponentDescription(String componentDescription) {
        this.componentDescription = componentDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
