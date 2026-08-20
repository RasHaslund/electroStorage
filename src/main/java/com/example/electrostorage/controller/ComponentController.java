package com.example.electrostorage.controller;

import com.example.electrostorage.dto.CreateComponentRequest;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.service.ComponentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/components")
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping
    public List<ComponentModel> getAllComponents() {
        return componentService.getAllComponents();
    }

    @PostMapping
    public ResponseEntity<ComponentModel> createComponent(@RequestBody CreateComponentRequest request) {
        ComponentModel component = componentService.createComponent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(component);
    }

    @PatchMapping("/{id}/discontinued")
    public ComponentModel markAsDiscontinued(@PathVariable Long id) {
        return componentService.markAsDiscontinued(id);
    }
}
