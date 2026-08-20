package com.example.electrostorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parts_list")
public class PartsListModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "result_component_id")
    private ComponentModel resultComponent;

    public PartsListModel() {
    }

    public PartsListModel(String name, ComponentModel resultComponent) {
        this.name = name;
        this.resultComponent = resultComponent;
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

    public ComponentModel getResultComponent() {
        return resultComponent;
    }

    public void setResultComponent(ComponentModel resultComponent) {
        this.resultComponent = resultComponent;
    }
}
