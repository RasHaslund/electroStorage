package com.example.electrostorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "component")
public class ComponentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer internalNumber;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierModel supplier;

    private String externalPartNumber;
    private String description;
    private boolean discontinued;

    public ComponentModel() {
    }

    public ComponentModel(Integer internalNumber, SupplierModel supplier, String externalPartNumber, String description, boolean discontinued) {
        this.internalNumber = internalNumber;
        this.supplier = supplier;
        this.externalPartNumber = externalPartNumber;
        this.description = description;
        this.discontinued = discontinued;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(Integer internalNumber) {
        this.internalNumber = internalNumber;
    }

    public SupplierModel getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierModel supplier) {
        this.supplier = supplier;
    }

    public String getExternalPartNumber() {
        return externalPartNumber;
    }

    public void setExternalPartNumber(String externalPartNumber) {
        this.externalPartNumber = externalPartNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }
}
