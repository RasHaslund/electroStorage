let isAdmin = false;

document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#componentForm").addEventListener("submit", createComponent);
    document.querySelector("#produceAssemblyForm").addEventListener("submit", produceAssembly);
    loadPage();
});

// Henter admin-status og starter dataindlæsningen på siden
async function loadPage() {
    const status = await getAuthStatus();
    isAdmin = status.admin;
    setAdminControlsVisible(isAdmin);
    loadComponents();
    loadAssemblies();
}

// Henter komponenterne og viser dem i tabellen
async function loadComponents() {
    const response = await fetch("/components");

    if (!response.ok) {
        alert("Kunne ikke hente komponenter");
        return;
    }

    const components = await response.json();
    displayComponents(components);
}

// Bygger komponenttabellen og viser admin-knapper ved behov
function displayComponents(components) {
    const tableBody = document.querySelector("#componentTableBody");
    tableBody.innerHTML = "";

    components.forEach(component => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${component.id}</td>
            <td>${component.internalNumber}</td>
            <td>${getSupplierText(component.supplier)}</td>
            <td>${component.externalPartNumber || ""}</td>
            <td>${component.description || ""}</td>
            <td>${component.stockQuantity}</td>
            <td>${component.discontinued ? "Ja" : "Nej"}</td>
            <td></td>
        `;

        if (isAdmin) {
            const button = document.createElement("button");
            button.textContent = "Marker udgået";
            button.disabled = component.discontinued;
            button.addEventListener("click", () => markAsDiscontinued(component.id));

            row.querySelector("td:last-child").appendChild(button);
        }

        tableBody.appendChild(row);
    });
}

// Sender formularen til backend og genindlæser komponentlisten
async function createComponent(event) {
    event.preventDefault();

    const request = {
        internalNumber: Number(document.querySelector("#internalNumber").value),
        supplierId: Number(document.querySelector("#supplierId").value),
        externalPartNumber: document.querySelector("#externalPartNumber").value,
        description: document.querySelector("#description").value
    };

    const response = await fetch("/components", {
        method: "POST",
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke oprette komponent");
        return;
    }

    document.querySelector("#componentForm").reset();
    loadComponents();
}

// Marker en komponent som udgået via backend
async function markAsDiscontinued(id) {
    const response = await fetch(`/components/${id}/discontinued`, {
        method: "PATCH",
        credentials: "same-origin"
    });

    if (!response.ok) {
        alert("Kunne ikke markere komponenten som udgået");
        return;
    }

    loadComponents();
}

// Henter styklister til dropdownen i "Saml komponent"
async function loadAssemblies() {
    const response = await fetch("/assemblies");

    if (!response.ok) {
        alert("Kunne ikke hente styklister");
        return;
    }

    const assemblies = await response.json();
    displayAssemblies(assemblies);
}

function displayAssemblies(assemblies) {
    const select = document.querySelector("#assemblyId");
    select.innerHTML = "";

    assemblies.forEach(assembly => {
        const option = document.createElement("option");
        option.value = assembly.id;
        option.textContent = assembly.name;
        select.appendChild(option);
    });
}

// Producerer en stykliste og opdaterer lager-visningen bagefter
async function produceAssembly(event) {
    event.preventDefault();

    const assemblyId = document.querySelector("#assemblyId").value;
    const request = {
        quantity: Number(document.querySelector("#assemblyQuantity").value)
    };

    const response = await fetch(`/assemblies/${assemblyId}/produce`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke samle komponent. Tjek at der er nok paa lager.");
        return;
    }

    document.querySelector("#produceAssemblyForm").reset();
    document.querySelector("#assemblyQuantity").value = 1;
    document.querySelector("#assemblyMessage").textContent = "Komponent samlet";
    loadComponents();
}

function getSupplierText(supplier) {
    if (!supplier) {
        return "";
    }

    return `${supplier.id} ${supplier.name || ""}`;
}
