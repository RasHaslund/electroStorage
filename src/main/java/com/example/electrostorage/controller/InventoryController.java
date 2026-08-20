package com.example.electrostorage.controller;

import com.example.electrostorage.dto.InventoryCountRequest;
import com.example.electrostorage.dto.InventoryOverviewResponse;
import com.example.electrostorage.model.InventoryCountItemModel;
import com.example.electrostorage.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryOverviewResponse> getInventoryOverview() {
        return inventoryService.getInventoryOverview();
    }

    @PostMapping("/count")
    public ResponseEntity<InventoryCountItemModel> registerCount(@RequestBody InventoryCountRequest request) {
        InventoryCountItemModel inventoryCountItem = inventoryService.registerCount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryCountItem);
    }
}
