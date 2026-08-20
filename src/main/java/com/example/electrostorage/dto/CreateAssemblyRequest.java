package com.example.electrostorage.dto;

import java.util.List;

public class CreateAssemblyRequest {

    private String name;
    private Long resultComponentId;
    private List<CreateAssemblyItemRequest> items;

    public CreateAssemblyRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getResultComponentId() {
        return resultComponentId;
    }

    public void setResultComponentId(Long resultComponentId) {
        this.resultComponentId = resultComponentId;
    }

    public List<CreateAssemblyItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateAssemblyItemRequest> items) {
        this.items = items;
    }
}
