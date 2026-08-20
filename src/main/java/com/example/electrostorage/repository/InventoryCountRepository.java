package com.example.electrostorage.repository;

import com.example.electrostorage.model.InventoryCountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryCountRepository extends JpaRepository<InventoryCountModel, Long> {
}
