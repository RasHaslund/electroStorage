package com.example.electrostorage.service;

import com.example.electrostorage.dto.CreateSupplierRequest;
import com.example.electrostorage.model.SupplierModel;
import com.example.electrostorage.repository.ComponentRepository;
import com.example.electrostorage.repository.PurchaseOrderRepository;
import com.example.electrostorage.repository.SupplierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ComponentRepository componentRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public SupplierService(SupplierRepository supplierRepository,
                           ComponentRepository componentRepository,
                           PurchaseOrderRepository purchaseOrderRepository) {
        this.supplierRepository = supplierRepository;
        this.componentRepository = componentRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<SupplierModel> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public SupplierModel createSupplier(CreateSupplierRequest request) {
        SupplierModel supplier = new SupplierModel(request.getName(), request.getAddress());
        return supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found");
        }

        if (componentRepository.existsBySupplier_Id(id) || purchaseOrderRepository.existsBySupplier_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Supplier is still in use");
        }

        supplierRepository.deleteById(id);
    }
}
