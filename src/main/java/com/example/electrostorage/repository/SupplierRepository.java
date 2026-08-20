package com.example.electrostorage.repository;

import com.example.electrostorage.model.SupplierModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<SupplierModel, Long> {
}
