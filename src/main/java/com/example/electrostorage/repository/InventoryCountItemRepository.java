package com.example.electrostorage.repository;

import com.example.electrostorage.model.InventoryCountItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryCountItemRepository extends JpaRepository<InventoryCountItemModel, Long> {
}
