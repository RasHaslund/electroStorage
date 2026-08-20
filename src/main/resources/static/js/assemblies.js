let components = [];

document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#assemblyForm").addEventListener("submit", createAssembly);
    document.querySelector("#addAssemblyItemButton").addEventListener("click", addAssemblyItemRow);

    loadComponents();
    loadAssemblies();
});

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
            <button type="button">Vis indhold</button>
        `;

        card.querySelector("button").addEventListener("click", () => loadAssemblyDetails(assembly.id));
        assemblyList.appendChild(card);
    });

    if (assemblies.length > 0) {
        loadAssemblyDetails(assemblies[0].id);
    }
}

async function loadAssemblyDetails(id) {
    const response = await fetch(`/assemblies/${id}`);

    if (!response.ok) {
        alert("Kunne ikke hente stykliste");
        return;
    }

    const assembly = await response.json();
    displayAssemblyDetails(assembly);
}

function displayAssemblyDetails(assembly) {
    const details = document.querySelector("#assemblyDetails");
    details.innerHTML = "";

    const title = document.createElement("h3");
    title.textContent = assembly.name;
    details.appendChild(title);

    const result = document.createElement("p");
    result.innerHTML = `<strong>Resultat:</strong> ${assembly.resultComponentDescription || ""}`;
    details.appendChild(result);

    const listTitle = document.createElement("p");
    listTitle.innerHTML = "<strong>Komponenter:</strong>";
    details.appendChild(listTitle);

    const list = document.createElement("ul");

    assembly.items.forEach(item => {
        const listItem = document.createElement("li");
        listItem.textContent = `${item.quantity} x ${item.description}`;
        list.appendChild(listItem);
    });

    details.appendChild(list);
}
