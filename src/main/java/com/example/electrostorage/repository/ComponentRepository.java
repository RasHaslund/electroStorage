package com.example.electrostorage.repository;

import com.example.electrostorage.model.ComponentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<ComponentModel, Long> {

    boolean existsBySupplier_Id(Long supplierId);
}
