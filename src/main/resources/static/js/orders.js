document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#orderForm").addEventListener("submit", createOrder);
    document.querySelector("#supplierForm").addEventListener("submit", createSupplier);
    loadSuppliers();
    loadOrders();
});

async function loadSuppliers() {
    const response = await fetch("/suppliers");

    if (!response.ok) {
        alert("Kunne ikke hente leverandører");
        return;
    }

    const suppliers = await response.json();
    displaySuppliers(suppliers);
}

function displaySuppliers(suppliers) {
    const select = document.querySelector("#supplierId");
    select.innerHTML = "";

    suppliers.forEach(supplier => {
        const option = document.createElement("option");
        option.value = supplier.id;
        option.textContent = supplier.name;
        select.appendChild(option);
    });
}

async function loadOrders() {
    const response = await fetch("/orders");

    if (!response.ok) {
        alert("Kunne ikke hente ordrer");
        return;
    }

    const orders = await response.json();
    const activeOrders = orders.filter(order => order.receivedDate === null && !order.cancelled);
    const receivedOrders = orders.filter(order => order.receivedDate !== null);
    const cancelledOrders = orders.filter(order => order.cancelled);

    displayActiveOrders(activeOrders);
    displayReceivedOrders(receivedOrders);
    displayCancelledOrders(cancelledOrders);
}

function displayActiveOrders(orders) {
    const tableBody = document.querySelector("#activeOrderTableBody");
    tableBody.innerHTML = "";

    orders.forEach(order => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${order.id}</td>
            <td>${getSupplierText(order.supplier)}</td>
            <td>${getOrderStatus(order)}</td>
            <td>${order.trackingCode || ""}</td>
            <td>${order.sentDate || ""}</td>
            <td>${order.expectedDeliveryDate || ""}</td>
            <td><a href="order.html?id=${order.id}">Åbn</a></td>
        `;

        tableBody.appendChild(row);
    });
}

function displayReceivedOrders(orders) {
    const tableBody = document.querySelector("#receivedOrderTableBody");
    tableBody.innerHTML = "";

    orders.forEach(order => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${order.id}</td>
            <td>${getSupplierText(order.supplier)}</td>
            <td>${order.trackingCode || ""}</td>
            <td>${order.receivedDate || ""}</td>
            <td><a href="order.html?id=${order.id}">Åbn</a></td>
        `;

        tableBody.appendChild(row);
    });
}

function displayCancelledOrders(orders) {
    const tableBody = document.querySelector("#cancelledOrderTableBody");
    tableBody.innerHTML = "";

    orders.forEach(order => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${order.id}</td>
            <td>${getSupplierText(order.supplier)}</td>
            <td><a href="order.html?id=${order.id}">Åbn</a></td>
        `;

        tableBody.appendChild(row);
    });
}

async function createOrder(event) {
    event.preventDefault();

    const request = {
        supplierId: Number(document.querySelector("#supplierId").value)
    };

    const response = await fetch("/orders", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke oprette ordre");
        return;
    }

    document.querySelector("#orderForm").reset();
    loadOrders();
}

async function createSupplier(event) {
    event.preventDefault();

    const request = {
        name: document.querySelector("#supplierName").value,
        address: document.querySelector("#supplierAddress").value
    };

    const response = await fetch("/suppliers", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke oprette leverandør");
        return;
    }

    document.querySelector("#supplierForm").reset();
    document.querySelector("#supplierMessage").textContent = "Leverandør oprettet";
    loadSuppliers();
}

function getOrderStatus(order) {
    if (order.cancelled) {
        return "Annulleret";
    }

    if (order.receivedDate !== null) {
        return "Modtaget";
    }

    if (order.sentDate !== null) {
        return "Sendt";
    }

    return "Kladde";
}

function getSupplierText(supplier) {
    if (!supplier) {
        return "";
    }

    return supplier.name || "";
}
