document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#inventoryCountForm").addEventListener("submit", registerInventoryCount);
    setDefaultCountedAt();
    loadComponents();
    loadInventory();
});

// Henter komponenter til lageroptællings-formularen
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

    components.forEach(component => {
        const option = document.createElement("option");
        option.value = component.id;
        option.textContent = component.description;
        select.appendChild(option);
    });
}

// Henter lageroversigten fra backend
async function loadInventory() {
    const response = await fetch("/inventory");

    if (!response.ok) {
        alert("Kunne ikke hente lageroversigt");
        return;
    }

    const inventory = await response.json();
    displayInventory(inventory);
}

// Viser lagerstatus og seneste optælling i tabellen
function displayInventory(inventory) {
    const tableBody = document.querySelector("#inventoryTableBody");
    tableBody.innerHTML = "";

    inventory.forEach(item => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${item.description || ""}</td>
            <td>${item.stockQuantity}</td>
            <td>${item.lastCountedBy || "-"}</td>
            <td>${formatDateTime(item.lastCountedAt)}</td>
        `;

        tableBody.appendChild(row);
    });
}

// Gemmer en optælling og opdaterer lageroversigten bagefter
async function registerInventoryCount(event) {
    event.preventDefault();

    const request = {
        componentId: Number(document.querySelector("#componentId").value),
        actualQuantity: Number(document.querySelector("#actualQuantity").value),
        countedBy: document.querySelector("#countedBy").value,
        countedAt: document.querySelector("#countedAt").value
    };

    const response = await fetch("/inventory/count", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke gemme optælling");
        return;
    }

    document.querySelector("#inventoryCountForm").reset();
    setDefaultCountedAt();
    document.querySelector("#inventoryMessage").textContent = "Optælling gemt";
    loadInventory();
}

// Sætter optællingstidspunktet til nu i inputfeltets format
function setDefaultCountedAt() {
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    document.querySelector("#countedAt").value = now.toISOString().slice(0, 16);
}

function formatDateTime(value) {
    if (!value) {
        return "-";
    }

    return value.replace("T", " ").slice(0, 16);
}
