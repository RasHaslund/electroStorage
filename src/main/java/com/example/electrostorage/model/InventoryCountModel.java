package com.example.electrostorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_count")
public class InventoryCountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countedBy;
    private LocalDateTime countedAt;

    public InventoryCountModel() {
    }

    public InventoryCountModel(String countedBy, LocalDateTime countedAt) {
        this.countedBy = countedBy;
        this.countedAt = countedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
