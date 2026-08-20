// Bygger den fælles header og opdaterer admin-visning
async function loadHeader() {
    const header = document.querySelector("#header");

    header.innerHTML = `
        <header class="site-header">
            <div class="inner">
                <a class="brand" href="index.html">ElektroStorage</a>
                <nav class="nav">
                    <a href="components.html">Komponenter</a>
                    <a href="orders.html">Ordrer</a>
                    <a href="inventory.html">Lager</a>
                    <a href="assemblies.html">Styklister</a>
                    <span id="authNavigation"></span>
                </nav>
            </div>
        </header>
    `;

    const status = await getAuthStatus();
    setAdminControlsVisible(status.admin);
    displayAuthNavigation(status);
    document.dispatchEvent(new CustomEvent("authStatusLoaded", { detail: status }));
}

loadHeader();

// Henter login-status, som frontend bruger til at vise admin-controls
async function getAuthStatus() {
    try {
        const response = await fetch("/api/auth/status", {
            credentials: "same-origin"
        });

        if (!response.ok) {
            return { authenticated: false, admin: false };
        }

        return await response.json();
    } catch (error) {
        return { authenticated: false, admin: false };
    }
}

// Viser eller skjuler elementer der kun er for ADMIN
function setAdminControlsVisible(admin) {
    document.querySelectorAll(".admin-only").forEach(element => {
        element.style.display = admin ? "block" : "none";
    });
}

// Viser enten login-link eller log ud-knap i headeren
function displayAuthNavigation(status) {
    const authNavigation = document.querySelector("#authNavigation");

    if (!authNavigation) {
        return;
    }

    if (!status.authenticated) {
        authNavigation.innerHTML = `<a href="login.html">Login</a>`;
        return;
    }

    authNavigation.innerHTML = `<button id="logoutButton" type="button">Log ud</button>`;
    document.querySelector("#logoutButton").addEventListener("click", logout);
}

// Logger ud ved at få backend til at fjerne JWT-cookien
async function logout() {
    await fetch("/api/auth/logout", {
        method: "POST",
        credentials: "same-origin"
    });

    window.location.href = "index.html";
}
