package com.example.electrostorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_count_item")
public class InventoryCountItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_count_id")
    private InventoryCountModel inventoryCount;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private ComponentModel component;

    private Integer actualQuantity;

    public InventoryCountItemModel() {
    }

    public InventoryCountItemModel(InventoryCountModel inventoryCount, ComponentModel component, Integer actualQuantity) {
        this.inventoryCount = inventoryCount;
        this.component = component;
        this.actualQuantity = actualQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryCountModel getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(InventoryCountModel inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public ComponentModel getComponent() {
        return component;
    }

    public void setComponent(ComponentModel component) {
        this.component = component;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }
}
