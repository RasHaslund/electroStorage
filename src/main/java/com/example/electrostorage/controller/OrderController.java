package com.example.electrostorage.controller;

import com.example.electrostorage.dto.AddOrderLineRequest;
import com.example.electrostorage.dto.CreateOrderRequest;
import com.example.electrostorage.dto.OrderResponse;
import com.example.electrostorage.dto.SendOrderRequest;
import com.example.electrostorage.dto.UpdateDeliveryInfoRequest;
import com.example.electrostorage.model.OrderLineModel;
import com.example.electrostorage.model.PurchaseOrderModel;
import com.example.electrostorage.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<PurchaseOrderModel> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderModel> createOrder(@RequestBody CreateOrderRequest request) {
        PurchaseOrderModel order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/{orderId}/components")
    public ResponseEntity<OrderLineModel> addComponentToOrder(@PathVariable Long orderId, @RequestBody AddOrderLineRequest request) {
        OrderLineModel orderLine = orderService.addComponentToOrder(orderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderLine);
    }

    @DeleteMapping("/{orderId}/components/{orderLineId}")
    public ResponseEntity<Void> removeComponentFromOrder(@PathVariable Long orderId, @PathVariable Long orderLineId) {
        orderService.removeComponentFromOrder(orderId, orderLineId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/send")
    public PurchaseOrderModel sendOrder(@PathVariable Long orderId, @RequestBody(required = false) SendOrderRequest request) {
        return orderService.sendOrder(orderId, request);
    }

    @PatchMapping("/{orderId}/delivery-info")
    public PurchaseOrderModel updateDeliveryInfo(@PathVariable Long orderId, @RequestBody UpdateDeliveryInfoRequest request) {
        return orderService.updateDeliveryInfo(orderId, request);
    }

    @PatchMapping("/{orderId}/cancel")
    public PurchaseOrderModel cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PatchMapping("/{orderId}/receive")
    public PurchaseOrderModel receiveOrder(@PathVariable Long orderId) {
        return orderService.receiveOrder(orderId);
    }
}
