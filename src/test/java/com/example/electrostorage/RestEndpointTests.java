package com.example.electrostorage;

import com.example.electrostorage.controller.AssemblyController;
import com.example.electrostorage.controller.ComponentController;
import com.example.electrostorage.controller.InventoryController;
import com.example.electrostorage.controller.OrderController;
import com.example.electrostorage.dto.AddOrderLineRequest;
import com.example.electrostorage.dto.AssemblyResponse;
import com.example.electrostorage.dto.CreateComponentRequest;
import com.example.electrostorage.dto.CreateOrderRequest;
import com.example.electrostorage.dto.InventoryCountRequest;
import com.example.electrostorage.dto.SendOrderRequest;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.InventoryCountItemModel;
import com.example.electrostorage.model.OrderLineModel;
import com.example.electrostorage.model.PurchaseOrderModel;
import com.example.electrostorage.model.SupplierModel;
import com.example.electrostorage.service.AssemblyService;
import com.example.electrostorage.service.ComponentService;
import com.example.electrostorage.service.InventoryService;
import com.example.electrostorage.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestEndpointTests {

    private MockMvc mockMvc;
    private ComponentService componentService;
    private OrderService orderService;
    private InventoryService inventoryService;
    private AssemblyService assemblyService;

    @BeforeEach
    void setup() {
        componentService = new TestComponentService();
        orderService = new TestOrderService();
        inventoryService = new TestInventoryService();
        assemblyService = new TestAssemblyService();

        mockMvc = MockMvcBuilders.standaloneSetup(
                new ComponentController(componentService),
                new OrderController(orderService),
                new InventoryController(inventoryService),
                new AssemblyController(assemblyService)
        ).build();
    }

    @Test
    void shouldGetAllComponents() throws Exception {
        mockMvc.perform(get("/components"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateComponent() throws Exception {
        mockMvc.perform(post("/components")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "internalNumber": 1001,
                                  "supplierId": 1,
                                  "externalPartNumber": "LED-RED-5MM",
                                  "description": "LED 5 mm, red"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldMarkComponentAsDiscontinued() throws Exception {
        mockMvc.perform(patch("/components/1/discontinued"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateOrder() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "supplierId": 1,
                                  "trackingCode": "TRACK-1001"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldAddComponentToOrder() throws Exception {
        mockMvc.perform(post("/orders/1/components")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "componentId": 1,
                                  "quantity": 10
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldSendOrder() throws Exception {
        mockMvc.perform(patch("/orders/1/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "expectedDeliveryDate": "2026-09-01"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetInventory() throws Exception {
        mockMvc.perform(get("/inventory"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRegisterInventoryCount() throws Exception {
        mockMvc.perform(post("/inventory/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "componentId": 1,
                                  "actualQuantity": 25,
                                  "countedBy": "Rasmus"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAssemblies() throws Exception {
        mockMvc.perform(get("/assemblies"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAssemblyById() throws Exception {
        mockMvc.perform(get("/assemblies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lysende LED"));
    }

    private static class TestComponentService extends ComponentService {

        TestComponentService() {
            super(null, null);
        }

        @Override
        public List<ComponentModel> getAllComponents() {
            return List.of();
        }

        @Override
        public ComponentModel createComponent(CreateComponentRequest request) {
            return new ComponentModel(
                    request.getInternalNumber(),
                    new SupplierModel("Test Supplier", "Test Address"),
                    request.getExternalPartNumber(),
                    request.getDescription(),
                    false
            );
        }

        @Override
        public ComponentModel markAsDiscontinued(Long id) {
            ComponentModel component = new ComponentModel();
            component.setDiscontinued(true);
            return component;
        }
    }

    private static class TestOrderService extends OrderService {

        TestOrderService() {
            super(null, null, null, null);
        }

        @Override
        public List<PurchaseOrderModel> getAllOrders() {
            return List.of();
        }

        @Override
        public PurchaseOrderModel createOrder(CreateOrderRequest request) {
            return new PurchaseOrderModel();
        }

        @Override
        public OrderLineModel addComponentToOrder(Long orderId, AddOrderLineRequest request) {
            return new OrderLineModel();
        }

        @Override
        public PurchaseOrderModel sendOrder(Long orderId, SendOrderRequest request) {
            return new PurchaseOrderModel();
        }
    }

    private static class TestInventoryService extends InventoryService {

        TestInventoryService() {
            super(null, null, null, null);
        }

        @Override
        public List<com.example.electrostorage.dto.InventoryOverviewResponse> getInventoryOverview() {
            return List.of();
        }

        @Override
        public InventoryCountItemModel registerCount(InventoryCountRequest request) {
            return new InventoryCountItemModel();
        }
    }

    private static class TestAssemblyService extends AssemblyService {

        TestAssemblyService() {
            super(null, null);
        }

        @Override
        public List<AssemblyResponse> getAllPartsLists() {
            return List.of();
        }

        @Override
        public AssemblyResponse getPartsList(Long id) {
            return new AssemblyResponse(id, "Lysende LED", 10L, "Lysende LED", List.of());
        }
    }
}
