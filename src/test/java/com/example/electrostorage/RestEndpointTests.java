package com.example.electrostorage;

import com.example.electrostorage.controller.AssemblyController;
import com.example.electrostorage.controller.ComponentController;
import com.example.electrostorage.controller.InventoryController;
import com.example.electrostorage.controller.OrderController;
import com.example.electrostorage.controller.SupplierController;
import com.example.electrostorage.dto.AddOrderLineRequest;
import com.example.electrostorage.dto.AssemblyResponse;
import com.example.electrostorage.dto.CreateAssemblyRequest;
import com.example.electrostorage.dto.CreateComponentRequest;
import com.example.electrostorage.dto.CreateOrderRequest;
import com.example.electrostorage.dto.CreateSupplierRequest;
import com.example.electrostorage.dto.InventoryCountRequest;
import com.example.electrostorage.dto.OrderLineResponse;
import com.example.electrostorage.dto.OrderResponse;
import com.example.electrostorage.dto.SendOrderRequest;
import com.example.electrostorage.dto.UpdateDeliveryInfoRequest;
import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.model.InventoryCountItemModel;
import com.example.electrostorage.model.OrderLineModel;
import com.example.electrostorage.model.PartsListModel;
import com.example.electrostorage.model.PurchaseOrderModel;
import com.example.electrostorage.model.SupplierModel;
import com.example.electrostorage.service.AssemblyService;
import com.example.electrostorage.service.ComponentService;
import com.example.electrostorage.service.InventoryService;
import com.example.electrostorage.service.OrderService;
import com.example.electrostorage.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private SupplierService supplierService;

    @BeforeEach
    void setup() {
        componentService = new TestComponentService();
        orderService = new TestOrderService();
        inventoryService = new TestInventoryService();
        assemblyService = new TestAssemblyService();
        supplierService = new TestSupplierService();

        mockMvc = MockMvcBuilders.standaloneSetup(
                new ComponentController(componentService),
                new OrderController(orderService),
                new InventoryController(inventoryService),
                new AssemblyController(assemblyService),
                new SupplierController(supplierService)
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
                                  "supplierId": 1
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplierName").value("Test Supplier"));
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
    void shouldNotAddComponentToSentOrder() throws Exception {
        mockMvc.perform(post("/orders/2/components")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "componentId": 1,
                                  "quantity": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRemoveComponentFromOrder() throws Exception {
        mockMvc.perform(delete("/orders/1/components/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotRemoveComponentFromSentOrder() throws Exception {
        mockMvc.perform(delete("/orders/2/components/1"))
                .andExpect(status().isBadRequest());
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
    void shouldUpdateDeliveryInfo() throws Exception {
        mockMvc.perform(patch("/orders/2/delivery-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "trackingCode": "TRACK-2002",
                                  "expectedDeliveryDate": "2026-09-01"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCancelOrder() throws Exception {
        mockMvc.perform(patch("/orders/1/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReceiveOrder() throws Exception {
        mockMvc.perform(patch("/orders/2/receive"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotReceiveOrderTwice() throws Exception {
        mockMvc.perform(patch("/orders/3/receive"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllSuppliers() throws Exception {
        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateSupplier() throws Exception {
        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "ElectroParts",
                                  "address": "Industrivej 10"
                                }
                                """))
                .andExpect(status().isCreated());
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
                                  "countedBy": "Rasmus",
                                  "countedAt": "2026-08-20T13:30:00"
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
    void shouldCreateAssembly() throws Exception {
        mockMvc.perform(post("/assemblies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Test stykliste",
                                  "resultComponentId": 1,
                                  "items": [
                                    {
                                      "componentId": 2,
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAssemblyById() throws Exception {
        mockMvc.perform(get("/assemblies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lysende LED"));
    }

    @Test
    void shouldProduceAssembly() throws Exception {
        mockMvc.perform(post("/assemblies/1/produce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 1
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenNotEnoughStock() throws Exception {
        mockMvc.perform(post("/assemblies/1/produce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 100
                                }
                                """))
                .andExpect(status().isBadRequest());
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
        public OrderResponse getOrder(Long orderId) {
            return new OrderResponse(
                    orderId,
                    "Test Supplier",
                    null,
                    null,
                    null,
                    null,
                    false,
                    List.of(new OrderLineResponse(1L, 1L, "LED", 10))
            );
        }

        @Override
        public OrderLineModel addComponentToOrder(Long orderId, AddOrderLineRequest request) {
            if (orderId == 2L) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Components cannot be added to an order that has been sent");
            }

            return new OrderLineModel();
        }

        @Override
        public void removeComponentFromOrder(Long orderId, Long orderLineId) {
            if (orderId == 2L) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Components cannot be removed from an order that has been sent");
            }
        }

        @Override
        public PurchaseOrderModel sendOrder(Long orderId, SendOrderRequest request) {
            return new PurchaseOrderModel();
        }

        @Override
        public PurchaseOrderModel updateDeliveryInfo(Long orderId, UpdateDeliveryInfoRequest request) {
            return new PurchaseOrderModel();
        }

        @Override
        public PurchaseOrderModel cancelOrder(Long orderId) {
            return new PurchaseOrderModel();
        }

        @Override
        public PurchaseOrderModel receiveOrder(Long orderId) {
            if (orderId == 3L) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order has already been received");
            }

            return new PurchaseOrderModel();
        }
    }

    private static class TestSupplierService extends SupplierService {

        TestSupplierService() {
            super(null);
        }

        @Override
        public List<SupplierModel> getAllSuppliers() {
            return List.of(new SupplierModel("Test Supplier", "Test Address"));
        }

        @Override
        public SupplierModel createSupplier(CreateSupplierRequest request) {
            return new SupplierModel(request.getName(), request.getAddress());
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
            super(null, null, null);
        }

        @Override
        public List<AssemblyResponse> getAllPartsLists() {
            return List.of();
        }

        @Override
        public PartsListModel createAssembly(CreateAssemblyRequest request) {
            return new PartsListModel(request.getName(), new ComponentModel());
        }

        @Override
        public AssemblyResponse getPartsList(Long id) {
            return new AssemblyResponse(id, "Lysende LED", 10L, "Lysende LED", List.of());
        }

        @Override
        public void produceAssembly(Long assemblyId, int quantity) {
            if (quantity > 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough components in stock");
            }
        }
    }
}
