package com.example.electrostorage.dto;

public class CreateSupplierRequest {

    private String name;
    private String address;

    public CreateSupplierRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
