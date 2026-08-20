const params = new URLSearchParams(window.location.search);
const orderId = params.get("id");

document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#addComponentForm").addEventListener("submit", addComponentToOrder);
    document.querySelector("#sendOrderForm").addEventListener("submit", sendOrder);
    document.querySelector("#deliveryInfoForm").addEventListener("submit", updateDeliveryInfo);
    document.querySelector("#receiveOrderButton").addEventListener("click", receiveOrder);
    document.querySelector("#cancelOrderButton").addEventListener("click", cancelOrder);

    loadOrder();
    loadComponents();
});

async function loadOrder() {
    if (!orderId) {
        document.querySelector("#orderDetails").textContent = "Mangler order id i URL'en.";
        hideAllActions();
        return;
    }

    const response = await fetch(`/orders/${orderId}`);

    if (!response.ok) {
        alert("Kunne ikke hente ordre");
        return;
    }

    const order = await response.json();
    displayOrder(order);
    displayOrderLines(order);
    updateVisibleActions(order);
}

function displayOrder(order) {
    document.querySelector("#orderDetails").innerHTML = `
        <div class="details">
            <p><strong>Ordre id:</strong> ${order.id}</p>
            <p><strong>Leverandør:</strong> ${order.supplierName || ""}</p>
            <p><strong>Status:</strong> ${getOrderStatus(order)}</p>
            <p><strong>Trackingkode:</strong> ${order.trackingCode || ""}</p>
            <p><strong>Sendt dato:</strong> ${order.sentDate || ""}</p>
            <p><strong>Forventet levering:</strong> ${order.expectedDeliveryDate || ""}</p>
            <p><strong>Modtaget dato:</strong> ${order.receivedDate || ""}</p>
        </div>
    `;

    document.querySelector("#trackingCode").value = order.trackingCode || "";
    document.querySelector("#expectedDeliveryDate").value = order.expectedDeliveryDate || "";
}

function displayOrderLines(order) {
    const tableBody = document.querySelector("#orderLineTableBody");
    tableBody.innerHTML = "";

    order.orderLines.forEach(orderLine => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${orderLine.componentDescription}</td>
            <td>${orderLine.quantity}</td>
            <td></td>
        `;

        if (isDraft(order)) {
            const button = document.createElement("button");
            button.textContent = "Fjern";
            button.addEventListener("click", () => removeOrderLine(orderLine.id));
            row.querySelector("td:last-child").appendChild(button);
        }

        tableBody.appendChild(row);
    });
}

function updateVisibleActions(order) {
    document.querySelector("#editOrderSection").style.display = isDraft(order) ? "block" : "none";
    document.querySelector("#sendOrderSection").style.display = isDraft(order) ? "block" : "none";
    document.querySelector("#deliveryInfoSection").style.display = isSent(order) ? "block" : "none";
    document.querySelector("#receiveOrderButton").style.display = isSent(order) ? "inline-block" : "none";
    document.querySelector("#cancelOrderButton").style.display = canCancel(order) ? "inline-block" : "none";
}

function hideAllActions() {
    document.querySelector("#editOrderSection").style.display = "none";
    document.querySelector("#sendOrderSection").style.display = "none";
    document.querySelector("#deliveryInfoSection").style.display = "none";
    document.querySelector("#orderActionsSection").style.display = "none";
}

async function loadComponents() {
    const response = await fetch("/components");

    if (!response.ok) {
        alert("Kunne ikke hente komponenter");
        return;
    }

    const components = await response.json();
    displayComponentOptions(components);
}

function displayComponentOptions(components) {
    const select = document.querySelector("#componentId");
    select.innerHTML = "";

    components
        .filter(component => !component.discontinued)
        .forEach(component => {
            const option = document.createElement("option");
            option.value = component.id;
            option.textContent = component.description;
            select.appendChild(option);
        });
}

async function addComponentToOrder(event) {
    event.preventDefault();

    const request = {
        componentId: Number(document.querySelector("#componentId").value),
        quantity: Number(document.querySelector("#quantity").value)
    };

    const response = await fetch(`/orders/${orderId}/components`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke tilføje komponent til ordren");
        return;
    }

    document.querySelector("#addComponentForm").reset();
    loadOrder();
}

async function removeOrderLine(orderLineId) {
    const response = await fetch(`/orders/${orderId}/components/${orderLineId}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        alert("Kunne ikke fjerne komponent fra ordren");
        return;
    }

    loadOrder();
}

async function sendOrder(event) {
    event.preventDefault();

    const response = await fetch(`/orders/${orderId}/send`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({})
    });

    if (!response.ok) {
        alert("Kunne ikke sende ordren");
        return;
    }

    loadOrder();
}

async function updateDeliveryInfo(event) {
    event.preventDefault();

    const expectedDeliveryDate = document.querySelector("#expectedDeliveryDate").value;
    const request = {
        trackingCode: document.querySelector("#trackingCode").value || null,
        expectedDeliveryDate: expectedDeliveryDate || null
    };

    const response = await fetch(`/orders/${orderId}/delivery-info`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke gemme leveringsinfo");
        return;
    }

    loadOrder();
}

async function receiveOrder() {
    const response = await fetch(`/orders/${orderId}/receive`, {
        method: "PATCH"
    });

    if (!response.ok) {
        alert("Kunne ikke markere ordren som modtaget");
        return;
    }

    loadOrder();
}

async function cancelOrder() {
    const response = await fetch(`/orders/${orderId}/cancel`, {
        method: "PATCH"
    });

    if (!response.ok) {
        alert("Kunne ikke annullere ordren");
        return;
    }

    loadOrder();
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

function isDraft(order) {
    return order.sentDate === null && !order.cancelled;
}

function isSent(order) {
    return order.sentDate !== null && order.receivedDate === null && !order.cancelled;
}

function canCancel(order) {
    return order.receivedDate === null && !order.cancelled;
}
