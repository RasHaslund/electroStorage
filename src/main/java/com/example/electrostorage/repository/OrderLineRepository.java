package com.example.electrostorage.repository;

import com.example.electrostorage.model.OrderLineModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLineModel, Long> {

    List<OrderLineModel> findByPurchaseOrder_Id(Long purchaseOrderId);

    List<OrderLineModel> findByPurchaseOrder_ReceivedDateIsNotNull();
}
