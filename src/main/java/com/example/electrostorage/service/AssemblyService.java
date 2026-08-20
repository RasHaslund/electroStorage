package com.example.electrostorage.service;

import com.example.electrostorage.dto.AssemblyItemResponse;
import com.example.electrostorage.dto.AssemblyResponse;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.PartsListItemModel;
import com.example.electrostorage.model.PartsListModel;
import com.example.electrostorage.repository.PartsListItemRepository;
import com.example.electrostorage.repository.PartsListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AssemblyService {

    private final PartsListRepository partsListRepository;
    private final PartsListItemRepository partsListItemRepository;

    public AssemblyService(PartsListRepository partsListRepository, PartsListItemRepository partsListItemRepository) {
        this.partsListRepository = partsListRepository;
        this.partsListItemRepository = partsListItemRepository;
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
