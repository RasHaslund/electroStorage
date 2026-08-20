package com.example.electrostorage.repository;

import com.example.electrostorage.model.PurchaseOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderModel, Long> {

    boolean existsBySupplier_Id(Long supplierId);
}
