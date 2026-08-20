let components = [];
let isAdmin = false;

document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#assemblyForm").addEventListener("submit", createAssembly);
    document.querySelector("#addAssemblyItemButton").addEventListener("click", addAssemblyItemRow);

    loadPage();
});

async function loadPage() {
    const status = await getAuthStatus();
    isAdmin = status.admin;
    setAdminControlsVisible(isAdmin);
    loadComponents();
    loadAssemblies();
}

async function loadComponents() {
    const response = await fetch("/components");

    if (!response.ok) {
        alert("Kunne ikke hente komponenter");
        return;
    }

    components = await response.json();
    displayResultComponentOptions();
    addAssemblyItemRow();
}

function displayResultComponentOptions() {
    const select = document.querySelector("#resultComponentId");
    select.innerHTML = "";

    components.forEach(component => {
        const option = document.createElement("option");
        option.value = component.id;
        option.textContent = component.description;
        select.appendChild(option);
    });
}

function addAssemblyItemRow() {
    const rows = document.querySelector("#assemblyItemRows");
    const row = document.createElement("div");
    row.className = "assembly-item-row";

    const componentSelect = document.createElement("select");
    componentSelect.className = "assemblyComponentId";
    componentSelect.required = true;

    components.forEach(component => {
        const option = document.createElement("option");
        option.value = component.id;
        option.textContent = component.description;
        componentSelect.appendChild(option);
    });

    const quantityInput = document.createElement("input");
    quantityInput.className = "assemblyItemQuantity";
    quantityInput.type = "number";
    quantityInput.min = "1";
    quantityInput.value = "1";
    quantityInput.required = true;

    const removeButton = document.createElement("button");
    removeButton.type = "button";
    removeButton.textContent = "Fjern";
    removeButton.addEventListener("click", () => row.remove());

    row.appendChild(componentSelect);
    row.appendChild(quantityInput);
    row.appendChild(removeButton);
    rows.appendChild(row);
}

async function createAssembly(event) {
    event.preventDefault();

    const name = document.querySelector("#assemblyName").value.trim();
    const itemRows = document.querySelectorAll(".assembly-item-row");

    if (name === "") {
        alert("Navn er påkrævet");
        return;
    }

    if (itemRows.length === 0) {
        alert("Stykliste skal have mindst én komponent");
        return;
    }

    const items = [];

    itemRows.forEach(row => {
        items.push({
            componentId: Number(row.querySelector(".assemblyComponentId").value),
            quantity: Number(row.querySelector(".assemblyItemQuantity").value)
        });
    });

    const invalidItem = items.find(item => item.quantity <= 0);

    if (invalidItem) {
        alert("Antal skal være større end 0");
        return;
    }

    const request = {
        name: name,
        resultComponentId: Number(document.querySelector("#resultComponentId").value),
        items: items
    };

    const response = await fetch("/assemblies", {
        method: "POST",
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        alert("Kunne ikke oprette stykliste");
        return;
    }

    document.querySelector("#assemblyForm").reset();
    document.querySelector("#assemblyItemRows").innerHTML = "";
    document.querySelector("#assemblyMessage").textContent = "Stykliste oprettet";
    addAssemblyItemRow();
    loadAssemblies();
}

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
    const assemblyList = document.querySelector("#assemblyList");
    assemblyList.innerHTML = "";

    assemblies.forEach(assembly => {
        const card = document.createElement("div");
        card.className = "assembly-card";

        card.innerHTML = `
            <h3>${assembly.name}</h3>
            <p>Resultat: ${assembly.resultComponentDescription || ""}</p>
            <ul></ul>
            <div class="button-row"></div>
        `;

        const itemList = card.querySelector("ul");

        assembly.items.forEach(item => {
            const listItem = document.createElement("li");
            listItem.textContent = `${item.quantity} x ${item.description}`;
            itemList.appendChild(listItem);
        });

        const buttonRow = card.querySelector(".button-row");

        if (isAdmin) {
            const deleteButton = document.createElement("button");
            deleteButton.type = "button";
            deleteButton.textContent = "Slet";
            deleteButton.addEventListener("click", () => deleteAssembly(assembly.id));
            buttonRow.appendChild(deleteButton);
        }

        assemblyList.appendChild(card);
    });
}

async function deleteAssembly(id) {
    const response = await fetch(`/assemblies/${id}`, {
        method: "DELETE",
        credentials: "same-origin"
    });

    if (!response.ok) {
        alert("Kunne ikke slette stykliste");
        return;
    }

    loadAssemblies();
}
