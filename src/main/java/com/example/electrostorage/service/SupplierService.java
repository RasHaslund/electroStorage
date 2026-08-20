package com.example.electrostorage.service;

import com.example.electrostorage.dto.CreateSupplierRequest;
import com.example.electrostorage.model.SupplierModel;
import com.example.electrostorage.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<SupplierModel> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public SupplierModel createSupplier(CreateSupplierRequest request) {
        SupplierModel supplier = new SupplierModel(request.getName(), request.getAddress());
        return supplierRepository.save(supplier);
    }
}
