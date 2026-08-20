package com.example.electrostorage.controller;

import com.example.electrostorage.dto.AssemblyResponse;
import com.example.electrostorage.service.AssemblyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    public AssemblyResponse getAssembly(@PathVariable Long id) {
        return assemblyService.getPartsList(id);
    }
}
