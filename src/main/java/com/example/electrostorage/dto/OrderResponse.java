package com.example.electrostorage.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderResponse {

    private Long id;
    private String supplierName;
    private LocalDate sentDate;
    private LocalDate expectedDeliveryDate;
    private String trackingCode;
    private LocalDate receivedDate;
    private boolean cancelled;
    private List<OrderLineResponse> orderLines;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String supplierName, LocalDate sentDate, LocalDate expectedDeliveryDate, String trackingCode, LocalDate receivedDate, boolean cancelled, List<OrderLineResponse> orderLines) {
        this.id = id;
        this.supplierName = supplierName;
        this.sentDate = sentDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.trackingCode = trackingCode;
        this.receivedDate = receivedDate;
        this.cancelled = cancelled;
        this.orderLines = orderLines;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public List<OrderLineResponse> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLineResponse> orderLines) {
        this.orderLines = orderLines;
    }
}
