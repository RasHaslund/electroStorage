package com.example.electrostorage.controller;

import com.example.electrostorage.dto.AssemblyResponse;
import com.example.electrostorage.dto.CreateAssemblyRequest;
import com.example.electrostorage.dto.ProduceAssemblyRequest;
import com.example.electrostorage.model.PartsListModel;
import com.example.electrostorage.service.AssemblyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assemblies")
public class AssemblyController {

    private final AssemblyService assemblyService;

    public AssemblyController(AssemblyService assemblyService) {
        this.assemblyService = assemblyService;
    }

    @GetMapping
    public List<AssemblyResponse> getAllAssemblies() {
        return assemblyService.getAllPartsLists();
    }

    @PostMapping
    public ResponseEntity<PartsListModel> createAssembly(@RequestBody CreateAssemblyRequest request) {
        PartsListModel partsList = assemblyService.createAssembly(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(partsList);
    }

    @GetMapping("/{id}")
    public AssemblyResponse getAssembly(@PathVariable Long id) {
        return assemblyService.getPartsList(id);
    }

    @PostMapping("/{id}/produce")
    public ResponseEntity<Void> produceAssembly(@PathVariable Long id, @RequestBody ProduceAssemblyRequest request) {
        assemblyService.produceAssembly(id, request.getQuantity());
        return ResponseEntity.ok().build();
    }
}
