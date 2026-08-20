package com.example.electrostorage.repository;

import com.example.electrostorage.model.InventoryCountItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryCountItemRepository extends JpaRepository<InventoryCountItemModel, Long> {

    Optional<InventoryCountItemModel> findFirstByComponent_IdOrderByInventoryCount_CountedAtDesc(Long componentId);
}
