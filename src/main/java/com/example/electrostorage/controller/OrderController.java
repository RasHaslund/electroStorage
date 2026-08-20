package com.example.electrostorage.controller;

import com.example.electrostorage.dto.AddOrderLineRequest;
import com.example.electrostorage.dto.CreateOrderRequest;
import com.example.electrostorage.dto.SendOrderRequest;
import com.example.electrostorage.model.OrderLineModel;
import com.example.electrostorage.model.PurchaseOrderModel;
import com.example.electrostorage.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/{orderId}/send")
    public PurchaseOrderModel sendOrder(@PathVariable Long orderId, @RequestBody(required = false) SendOrderRequest request) {
        return orderService.sendOrder(orderId, request);
    }
}
