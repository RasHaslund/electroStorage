package com.example.electrostorage.service;

import com.example.electrostorage.dto.AssemblyItemResponse;
import com.example.electrostorage.dto.AssemblyResponse;
import com.example.electrostorage.dto.CreateAssemblyItemRequest;
import com.example.electrostorage.dto.CreateAssemblyRequest;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.PartsListItemModel;
import com.example.electrostorage.model.PartsListModel;
import com.example.electrostorage.repository.ComponentRepository;
import com.example.electrostorage.repository.PartsListItemRepository;
import com.example.electrostorage.repository.PartsListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AssemblyService {

    private final PartsListRepository partsListRepository;
    private final PartsListItemRepository partsListItemRepository;
    private final ComponentRepository componentRepository;

    public AssemblyService(PartsListRepository partsListRepository,
                           PartsListItemRepository partsListItemRepository,
                           ComponentRepository componentRepository) {
        this.partsListRepository = partsListRepository;
        this.partsListItemRepository = partsListItemRepository;
        this.componentRepository = componentRepository;
    }

    public List<AssemblyResponse> getAllPartsLists() {
        return partsListRepository.findAll()
                .stream()
                .map(this::createAssemblyResponse)
                .toList();
    }

    public AssemblyResponse getPartsList(Long id) {
        PartsListModel partsList = partsListRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parts list not found"));

        return createAssemblyResponse(partsList);
    }

    @Transactional
    public PartsListModel createAssembly(CreateAssemblyRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one item is required");
        }

        ComponentModel resultComponent = componentRepository.findById(request.getResultComponentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result component not found"));

        PartsListModel partsList = new PartsListModel(request.getName(), resultComponent);
        PartsListModel savedPartsList = partsListRepository.save(partsList);

        for (CreateAssemblyItemRequest itemRequest : request.getItems()) {
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
            }

            ComponentModel component = componentRepository.findById(itemRequest.getComponentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Component not found"));

            PartsListItemModel item = new PartsListItemModel(
                    savedPartsList,
                    component,
                    itemRequest.getQuantity()
            );

            partsListItemRepository.save(item);
        }

        return savedPartsList;
    }

    @Transactional
    public void produceAssembly(Long assemblyId, int quantity) {
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }

        PartsListModel partsList = partsListRepository.findById(assemblyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parts list not found"));

        List<PartsListItemModel> items = partsListItemRepository.findByPartsList_Id(partsList.getId());

        boolean enoughStock = items.stream()
                .allMatch(item ->
                        item.getComponent().getStockQuantity() >= item.getQuantity() * quantity
                );

        if (!enoughStock) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough components in stock");
        }

        for (PartsListItemModel item : items) {
            ComponentModel component = item.getComponent();
            int usedQuantity = item.getQuantity() * quantity;
            component.setStockQuantity(component.getStockQuantity() - usedQuantity);
            componentRepository.save(component);
        }

        ComponentModel resultComponent = partsList.getResultComponent();
        resultComponent.setStockQuantity(resultComponent.getStockQuantity() + quantity);
        componentRepository.save(resultComponent);
    }

    private AssemblyResponse createAssemblyResponse(PartsListModel partsList) {
        ComponentModel resultComponent = partsList.getResultComponent();
        List<PartsListItemModel> partsListItems = partsListItemRepository.findByPartsList_Id(partsList.getId());

        List<AssemblyItemResponse> items = partsListItems.stream()
                .map(item -> new AssemblyItemResponse(
                        item.getComponent().getId(),
                        item.getComponent().getDescription(),
                        item.getQuantity()
                ))
                .toList();

        return new AssemblyResponse(
                partsList.getId(),
                partsList.getName(),
                resultComponent.getId(),
                resultComponent.getDescription(),
                items
        );
    }
}
