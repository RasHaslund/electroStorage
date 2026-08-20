package com.example.electrostorage.service;

import com.example.electrostorage.dto.CreateComponentRequest;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.SupplierModel;
import com.example.electrostorage.repository.ComponentRepository;
import com.example.electrostorage.repository.SupplierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final SupplierRepository supplierRepository;

    public ComponentService(ComponentRepository componentRepository, SupplierRepository supplierRepository) {
        this.componentRepository = componentRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<ComponentModel> getAllComponents() {
        return componentRepository.findAll();
    }

    public ComponentModel createComponent(CreateComponentRequest request) {
        SupplierModel supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));

        ComponentModel component = new ComponentModel(
                request.getInternalNumber(),
                supplier,
                request.getExternalPartNumber(),
                request.getDescription(),
                false
        );

        return componentRepository.save(component);
    }

    public ComponentModel markAsDiscontinued(Long id) {
        ComponentModel component = componentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Component not found"));

        component.setDiscontinued(true);
        return componentRepository.save(component);
    }
}
