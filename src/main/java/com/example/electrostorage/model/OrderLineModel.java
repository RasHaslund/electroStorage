package com.example.electrostorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_line")
public class OrderLineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrderModel purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private ComponentModel component;

    private Integer quantity;

    public OrderLineModel() {
    }

    public OrderLineModel(PurchaseOrderModel purchaseOrder, ComponentModel component, Integer quantity) {
        this.purchaseOrder = purchaseOrder;
        this.component = component;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrderModel getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderModel purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
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
