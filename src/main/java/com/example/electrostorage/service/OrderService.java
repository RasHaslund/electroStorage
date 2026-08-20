package com.example.electrostorage.service;

import com.example.electrostorage.dto.AddOrderLineRequest;
import com.example.electrostorage.dto.CreateOrderRequest;
import com.example.electrostorage.dto.SendOrderRequest;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.OrderLineModel;
import com.example.electrostorage.model.PurchaseOrderModel;
import com.example.electrostorage.model.SupplierModel;
import com.example.electrostorage.repository.ComponentRepository;
import com.example.electrostorage.repository.OrderLineRepository;
import com.example.electrostorage.repository.PurchaseOrderRepository;
import com.example.electrostorage.repository.SupplierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ComponentRepository componentRepository;
    private final SupplierRepository supplierRepository;

    public OrderService(PurchaseOrderRepository purchaseOrderRepository,
                        OrderLineRepository orderLineRepository,
                        ComponentRepository componentRepository,
                        SupplierRepository supplierRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderLineRepository = orderLineRepository;
        this.componentRepository = componentRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<PurchaseOrderModel> getAllOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrderModel createOrder(CreateOrderRequest request) {
        SupplierModel supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));

        PurchaseOrderModel order = new PurchaseOrderModel(
                supplier,
                request.getTrackingCode(),
                null,
                null,
                null
        );

        return purchaseOrderRepository.save(order);
    }

    public OrderLineModel addComponentToOrder(Long orderId, AddOrderLineRequest request) {
        PurchaseOrderModel order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        ComponentModel component = componentRepository.findById(request.getComponentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Component not found"));

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than 0");
        }

        if (component.isDiscontinued()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Discontinued components cannot be ordered");
        }

        if (order.getSentDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Components cannot be added to an order that has been sent");
        }

        OrderLineModel orderLine = new OrderLineModel(order, component, request.getQuantity());
        return orderLineRepository.save(orderLine);
    }

    public PurchaseOrderModel sendOrder(Long orderId, SendOrderRequest request) {
        PurchaseOrderModel order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getSentDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order has already been sent");
        }

        order.setSentDate(LocalDate.now());

        if (request != null) {
            order.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        }

        return purchaseOrderRepository.save(order);
    }
}
