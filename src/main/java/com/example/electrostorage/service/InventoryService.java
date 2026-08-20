package com.example.electrostorage.service;

import com.example.electrostorage.dto.InventoryCountRequest;
import com.example.electrostorage.dto.InventoryOverviewResponse;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.InventoryCountItemModel;
import com.example.electrostorage.model.InventoryCountModel;
import com.example.electrostorage.repository.ComponentRepository;
import com.example.electrostorage.repository.InventoryCountItemRepository;
import com.example.electrostorage.repository.InventoryCountRepository;
import com.example.electrostorage.repository.OrderLineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return componentRepository.findAll()
                .stream()
                .map(this::createInventoryOverviewResponse)
                .toList();
    }

    @Transactional
    public InventoryCountItemModel registerCount(InventoryCountRequest request) {
        ComponentModel component = componentRepository.findById(request.getComponentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Component not found"));

        if (request.getActualQuantity() == null || request.getActualQuantity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Actual quantity cannot be negative");
        }

        LocalDateTime countedAt = request.getCountedAt();

        if (countedAt == null) {
            countedAt = LocalDateTime.now();
        }

        InventoryCountModel inventoryCount = new InventoryCountModel(request.getCountedBy(), countedAt);
        InventoryCountModel savedInventoryCount = inventoryCountRepository.save(inventoryCount);

        InventoryCountItemModel item = new InventoryCountItemModel(
                savedInventoryCount,
                component,
                request.getActualQuantity()
        );

        InventoryCountItemModel savedItem = inventoryCountItemRepository.save(item);

        component.setStockQuantity(request.getActualQuantity());
        componentRepository.save(component);

        return savedItem;
    }

    private InventoryOverviewResponse createInventoryOverviewResponse(ComponentModel component) {
        Optional<InventoryCountItemModel> latestCount = inventoryCountItemRepository
                .findFirstByComponent_IdOrderByInventoryCount_CountedAtDesc(component.getId());

        String lastCountedBy = "-";
        LocalDateTime lastCountedAt = null;

        if (latestCount.isPresent()) {
            InventoryCountModel inventoryCount = latestCount.get().getInventoryCount();
            lastCountedBy = inventoryCount.getCountedBy();
            lastCountedAt = inventoryCount.getCountedAt();
        }

        return new InventoryOverviewResponse(
                component.getId(),
                component.getDescription(),
                component.getStockQuantity(),
                lastCountedBy,
                lastCountedAt
        );
    }
}
