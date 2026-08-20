package com.example.electrostorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parts_list_item")
public class PartsListItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parts_list_id")
    private PartsListModel partsList;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private ComponentModel component;

    private Integer quantity;

    public PartsListItemModel() {
    }

    public PartsListItemModel(PartsListModel partsList, ComponentModel component, Integer quantity) {
        this.partsList = partsList;
        this.component = component;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartsListModel getPartsList() {
        return partsList;
    }

    public void setPartsList(PartsListModel partsList) {
        this.partsList = partsList;
    }

    public ComponentModel getComponent() {
        return component;
    }

    public void setComponent(ComponentModel component) {
        this.component = component;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
