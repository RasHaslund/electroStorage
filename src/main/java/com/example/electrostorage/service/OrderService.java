package com.example.electrostorage.service;

import com.example.electrostorage.dto.AddOrderLineRequest;
import com.example.electrostorage.dto.CreateOrderRequest;
import com.example.electrostorage.dto.OrderLineResponse;
import com.example.electrostorage.dto.OrderResponse;
import com.example.electrostorage.dto.SendOrderRequest;
import com.example.electrostorage.dto.UpdateDeliveryInfoRequest;
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
import org.springframework.transaction.annotation.Transactional;
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

    public OrderResponse getOrder(Long orderId) {
        PurchaseOrderModel order = findOrder(orderId);
        return createOrderResponse(order);
    }

    public PurchaseOrderModel createOrder(CreateOrderRequest request) {
        SupplierModel supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));

        PurchaseOrderModel order = new PurchaseOrderModel(
                supplier,
                null,
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

        if (order.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Components cannot be added to a cancelled order");
        }

        OrderLineModel orderLine = new OrderLineModel(order, component, request.getQuantity());
        return orderLineRepository.save(orderLine);
    }

    public void removeComponentFromOrder(Long orderId, Long orderLineId) {
        PurchaseOrderModel order = findOrder(orderId);
        OrderLineModel orderLine = orderLineRepository.findById(orderLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order line not found"));

        if (orderLine.getPurchaseOrder() == null || !orderLine.getPurchaseOrder().getId().equals(order.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order line does not belong to this order");
        }

        if (order.getSentDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Components cannot be removed from an order that has been sent");
        }

        if (order.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Components cannot be removed from a cancelled order");
        }

        orderLineRepository.delete(orderLine);
    }

    public PurchaseOrderModel sendOrder(Long orderId, SendOrderRequest request) {
        PurchaseOrderModel order = findOrder(orderId);

        if (order.getSentDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order has already been sent");
        }

        if (order.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cancelled orders cannot be sent");
        }

        order.setSentDate(LocalDate.now());
        return purchaseOrderRepository.save(order);
    }

    public PurchaseOrderModel updateDeliveryInfo(Long orderId, UpdateDeliveryInfoRequest request) {
        PurchaseOrderModel order = findOrder(orderId);

        if (order.getSentDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be sent before delivery info can be updated");
        }

        if (order.getReceivedDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Received orders cannot be changed");
        }

        if (order.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cancelled orders cannot be changed");
        }

        order.setTrackingCode(request.getTrackingCode());
        order.setExpectedDeliveryDate(request.getExpectedDeliveryDate());

        return purchaseOrderRepository.save(order);
    }

    public PurchaseOrderModel cancelOrder(Long orderId) {
        PurchaseOrderModel order = findOrder(orderId);

        if (order.getReceivedDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Received orders cannot be cancelled");
        }

        order.setCancelled(true);
        return purchaseOrderRepository.save(order);
    }

    @Transactional
    public PurchaseOrderModel receiveOrder(Long orderId) {
        PurchaseOrderModel order = findOrder(orderId);

        if (order.getSentDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be sent before it can be received");
        }

        if (order.getReceivedDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order has already been received");
        }

        if (order.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cancelled orders cannot be received");
        }

        List<OrderLineModel> orderLines = orderLineRepository.findByPurchaseOrder_Id(order.getId());

        for (OrderLineModel orderLine : orderLines) {
            ComponentModel component = orderLine.getComponent();
            component.setStockQuantity(component.getStockQuantity() + orderLine.getQuantity());
            componentRepository.save(component);
        }

        order.setReceivedDate(LocalDate.now());
        return purchaseOrderRepository.save(order);
    }

    private PurchaseOrderModel findOrder(Long orderId) {
        return purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    private OrderResponse createOrderResponse(PurchaseOrderModel order) {
        List<OrderLineResponse> orderLines = orderLineRepository.findByPurchaseOrder_Id(order.getId())
                .stream()
                .map(orderLine -> new OrderLineResponse(
                        orderLine.getId(),
                        orderLine.getComponent().getId(),
                        orderLine.getComponent().getDescription(),
                        orderLine.getQuantity()
                ))
                .toList();

        String supplierName = "";

        if (order.getSupplier() != null) {
            supplierName = order.getSupplier().getName();
        }

        return new OrderResponse(
                order.getId(),
                supplierName,
                order.getSentDate(),
                order.getExpectedDeliveryDate(),
                order.getTrackingCode(),
                order.getReceivedDate(),
                order.isCancelled(),
                orderLines
        );
    }
}
