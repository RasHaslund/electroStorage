package com.example.electrostorage.repository;

import com.example.electrostorage.model.PartsListItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartsListItemRepository extends JpaRepository<PartsListItemModel, Long> {

    List<PartsListItemModel> findByPartsList_Id(Long partsListId);
}
