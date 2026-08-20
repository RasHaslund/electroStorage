function loadHeader() {
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
                </nav>
            </div>
        </header>
    `;
}

loadHeader();
