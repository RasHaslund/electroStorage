package com.example.electrostorage.dto;

import java.util.List;

public class AssemblyResponse {

    private Long id;
    private String name;
    private Long resultComponentId;
    private String resultComponentDescription;
    private List<AssemblyItemResponse> items;

    public AssemblyResponse() {
    }

    public AssemblyResponse(Long id, String name, Long resultComponentId, String resultComponentDescription, List<AssemblyItemResponse> items) {
        this.id = id;
        this.name = name;
        this.resultComponentId = resultComponentId;
        this.resultComponentDescription = resultComponentDescription;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getResultComponentDescription() {
        return resultComponentDescription;
    }

    public void setResultComponentDescription(String resultComponentDescription) {
        this.resultComponentDescription = resultComponentDescription;
    }

    public List<AssemblyItemResponse> getItems() {
        return items;
    }

    public void setItems(List<AssemblyItemResponse> items) {
        this.items = items;
    }
}
