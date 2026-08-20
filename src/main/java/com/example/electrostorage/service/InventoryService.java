package com.example.electrostorage.service;

import com.example.electrostorage.dto.InventoryCountRequest;
import com.example.electrostorage.dto.InventoryOverviewResponse;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.InventoryCountItemModel;
import com.example.electrostorage.model.InventoryCountModel;
import com.example.electrostorage.model.OrderLineModel;
import com.example.electrostorage.repository.ComponentRepository;
import com.example.electrostorage.repository.InventoryCountItemRepository;
import com.example.electrostorage.repository.InventoryCountRepository;
import com.example.electrostorage.repository.OrderLineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    private final OrderLineRepository orderLineRepository;
    private final ComponentRepository componentRepository;
    private final InventoryCountRepository inventoryCountRepository;
    private final InventoryCountItemRepository inventoryCountItemRepository;

    public InventoryService(OrderLineRepository orderLineRepository,
                            ComponentRepository componentRepository,
                            InventoryCountRepository inventoryCountRepository,
                            InventoryCountItemRepository inventoryCountItemRepository) {
        this.orderLineRepository = orderLineRepository;
        this.componentRepository = componentRepository;
        this.inventoryCountRepository = inventoryCountRepository;
        this.inventoryCountItemRepository = inventoryCountItemRepository;
    }

    public List<InventoryOverviewResponse> getInventoryOverview() {
        List<OrderLineModel> receivedOrderLines = orderLineRepository.findByPurchaseOrder_ReceivedDateIsNotNull();
        Map<Long, InventoryOverviewResponse> overview = new LinkedHashMap<>();

        for (OrderLineModel orderLine : receivedOrderLines) {
            ComponentModel component = orderLine.getComponent();
            Integer quantity = orderLine.getQuantity();

            if (component == null || quantity == null) {
                continue;
            }

            InventoryOverviewResponse existing = overview.get(component.getId());

            if (existing == null) {
                overview.put(
                        component.getId(),
                        new InventoryOverviewResponse(component.getId(), component.getDescription(), quantity)
                );
            } else {
                existing.setTotalReceivedQuantity(existing.getTotalReceivedQuantity() + quantity);
            }
        }

        return new ArrayList<>(overview.values());
    }

    public InventoryCountItemModel registerCount(InventoryCountRequest request) {
        ComponentModel component = componentRepository.findById(request.getComponentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Component not found"));

        if (request.getActualQuantity() == null || request.getActualQuantity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Actual quantity cannot be negative");
        }

        InventoryCountModel inventoryCount = new InventoryCountModel(request.getCountedBy(), LocalDateTime.now());
        InventoryCountModel savedInventoryCount = inventoryCountRepository.save(inventoryCount);

        InventoryCountItemModel item = new InventoryCountItemModel(
                savedInventoryCount,
                component,
                request.getActualQuantity()
        );

        return inventoryCountItemRepository.save(item);
    }
}
