let isAdmin = false;

document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#orderForm").addEventListener("submit", createOrder);
    document.querySelector("#supplierForm").addEventListener("submit", createSupplier);
    loadPage();
});

// Henter admin-status og starter leverandør- og ordrevisningen
async function loadPage() {
    const status = await getAuthStatus();
    isAdmin = status.admin;
    setAdminControlsVisible(isAdmin);
    loadSuppliers();
    loadOrders();
}

// Henter leverandører til både ordre-formular og leverandørlisten
async function loadSuppliers() {
    const response = await fetch("/suppliers");

    if (!response.ok) {
        alert("Kunne ikke hente leverandører");
        return;
    }

    const suppliers = await response.json();
    displaySuppliers(suppliers);
}

// Viser leverandører og tilføjer Slet-knapper for ADMIN
function displaySuppliers(suppliers) {
    const select = document.querySelector("#supplierId");
    select.innerHTML = "";

    const supplierTableBody = document.querySelector("#supplierTableBody");
    if (supplierTableBody) {
        supplierTableBody.innerHTML = "";
    }

    suppliers.forEach(supplier => {
        const option = document.createElement("option");
        option.value = supplier.id;
        option.textContent = supplier.name;
        select.appendChild(option);

        if (supplierTableBody) {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${supplier.id}</td>
                <td>${supplier.name || ""}</td>
                <td>${supplier.address || ""}</td>
                <td></td>
            `;

            if (isAdmin) {
                const button = document.createElement("button");
                button.textContent = "Slet";
                button.addEventListener("click", () => deleteSupplier(supplier.id));
                row.querySelector("td:last-child").appendChild(button);
            }

            supplierTableBody.appendChild(row);
        }
    });
}

// Henter alle ordrer og deler dem op efter status
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

// Opretter en ny ordre og opdaterer oversigten
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

// Opretter en leverandør og genindlæser leverandørlisten
async function createSupplier(event) {
    event.preventDefault();

    const request = {
        name: document.querySelector("#supplierName").value,
        address: document.querySelector("#supplierAddress").value
    };

    const response = await fetch("/suppliers", {
        method: "POST",
        credentials: "same-origin",
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

// Sletter en leverandør, hvis backend tillader det
async function deleteSupplier(id) {
    const response = await fetch(`/suppliers/${id}`, {
        method: "DELETE",
        credentials: "same-origin"
    });

    if (!response.ok) {
        alert("Kunne ikke slette leverandør. Den kan være i brug.");
        return;
    }

    loadSuppliers();
}

// Omsætter datoer og flag til en tekststatus på siden
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
