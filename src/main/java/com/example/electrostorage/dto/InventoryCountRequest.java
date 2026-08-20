package com.example.electrostorage.dto;

import java.time.LocalDateTime;

public class InventoryCountRequest {

    private Long componentId;
    private Integer actualQuantity;
    private String countedBy;
    private LocalDateTime countedAt;

    public InventoryCountRequest() {
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getCountedBy() {
        return countedBy;
    }

    public void setCountedBy(String countedBy) {
        this.countedBy = countedBy;
    }

    public LocalDateTime getCountedAt() {
        return countedAt;
    }

    public void setCountedAt(LocalDateTime countedAt) {
        this.countedAt = countedAt;
    }
}
