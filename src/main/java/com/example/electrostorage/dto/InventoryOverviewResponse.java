package com.example.electrostorage.dto;

import java.time.LocalDateTime;

public class InventoryOverviewResponse {

    private Long componentId;
    private String description;
    private int stockQuantity;
    private String lastCountedBy;
    private LocalDateTime lastCountedAt;

    public InventoryOverviewResponse() {
    }

    public InventoryOverviewResponse(Long componentId, String description, int stockQuantity, String lastCountedBy, LocalDateTime lastCountedAt) {
        this.componentId = componentId;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.lastCountedBy = lastCountedBy;
        this.lastCountedAt = lastCountedAt;
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getLastCountedBy() {
        return lastCountedBy;
    }

    public void setLastCountedBy(String lastCountedBy) {
        this.lastCountedBy = lastCountedBy;
    }

    public LocalDateTime getLastCountedAt() {
        return lastCountedAt;
    }

    public void setLastCountedAt(LocalDateTime lastCountedAt) {
        this.lastCountedAt = lastCountedAt;
    }
}
