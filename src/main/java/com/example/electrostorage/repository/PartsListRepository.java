package com.example.electrostorage.repository;

import com.example.electrostorage.model.PartsListModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartsListRepository extends JpaRepository<PartsListModel, Long> {
}
