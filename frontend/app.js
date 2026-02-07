document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "https://web-3.local.test/api";

  // ======================
  // API CALLS
  // ======================

  function getDinos() {
    return fetch(`${API_BASE}/dinos`).then((response) => {
      if (!response.ok) {
        throw new Error("Failed to fetch dinos");
      }
      return response.json();
    });
  }

  function addDino(dino) {
    return fetch(`${API_BASE}/dinos`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dino),
    });
  }

  function removeDino(dinoData) {
    return fetch(`${API_BASE}/dinos/remove`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dinoData),
    }).then((response) => {
      if (!response.ok) {
        throw new Error("Failed to remove dinosaur");
      }
      return response.json();
    });
  }

  function getSpecies() {
    return fetch(`${API_BASE}/api/species`).then((response) => {
      if (!response.ok) {
        throw new Error("Failed to fetch species");
      }
      return response.json();
    });
  }

  function removeAllDino() {
    return fetch(`${API_BASE}/dinos/removeAll`, {
      method: "POST",
    }).then((response) => {
      if (!response.ok) {
        throw new Error("Failed to remove all dinosaurs");
      }
    });
  }

  function feedAll() {
    return fetch(`${API_BASE}/feed`, {
      method: "GET",
    });
  }

  // ======================
  // RENDER
  // ======================

  function render(dinos) {
    const container = document.getElementById("dino-container");

    if (!container) {
      console.error("❌ dino-container not found");
      return;
    }

    container.innerHTML = "";

    if (!Array.isArray(dinos) || dinos.length === 0) {
      container.innerHTML = "<p>No dinosaurs found 🦕</p>";
      return;
    }

    dinos.forEach((dino) => {
      const card = document.createElement("div");
      card.className = "dino-card";

      const dinoClass = dino.dangerCategory
        ? dino.dangerCategory.toLowerCase()
        : dino.dangerLevel > 7
          ? "HIGH"
          : dino.dangerLevel > 3
            ? "MEDIUM"
            : "LOW";

      const dangerClass = dino.dangerCategory ?? dinoClass.toLowerCase();

      card.innerHTML = `
                <div class="dino-name"><img src="images/${dino.espece}.jpeg" alt="${dino.name} Picture" /> ${dino.name}</div>
                <div class="species">🧬 Species: ${dino.espece}</div>
                <div class="energy">⚡ Energy: ${dino.energy}</div>
                <div class="danger ${dangerClass}">
                    ☠️ Danger: ${dinoClass ?? "MEDIUM"} (${dino.dangerLevel}/10)
                </div>
                <div>Status: ${dino.alive ? "✅ Alive" : "💀 Dead"}</div>
                ${
                  !dino.alive
                    ? `<div class="delete-btn-container delete" data-name="${dino.name}">
                    <i class="hgi hgi-stroke hgi-delete-02">Remove</i>
                </div>`
                    : ""
                }
            `;

      container.appendChild(card);
    });

    const removeBtns = document.querySelectorAll(".delete-btn-container");
    console.log(removeBtns);
    // Attach listeners
    removeBtns.forEach((btn) => {
      btn.addEventListener("click", () => {
        const name = btn.dataset.name;
        handleRemove(name);
      });
    });
  }

  // ======================
  // LOAD / REFRESH
  // ======================

  function loadDinos() {
    getDinos()
      .then((dinos) => {
        console.log("✅ Dinos loaded:", dinos);
        render(dinos);
      })
      .catch((error) => {
        console.error("❌ API error:", error);
      });
  }

  // ======================
  // EVENTS
  // ======================

  // ➕ Add Dino
  const form = document.getElementById("addDinoForm");

  form.addEventListener("submit", (event) => {
    event.preventDefault();

    const name = document.getElementById("dinoName").value.trim();
    const species = document.getElementById("dinoSpecies").value;
    const energyValue = document.getElementById("dinoEnergy").value;
    const dangerValue = document.getElementById("dinoDanger").value;

    if (!name || !species) {
      alert("Dino name and species are required");
      return;
    }

    const newDino = {
      name: name,
      espece: species,
      ...(energyValue !== "" && { energy: Number(energyValue) }),
      ...(dangerValue !== "" && { dangerLevel: Number(dangerValue) }),
    };

    addDino(newDino)
      .then(() => {
        console.log("✅ Dino added");
        form.reset();
        loadDinos();
      })
      .catch((err) => console.error("❌ Add dino error:", err));
  });

  // 🍖 Feed All
  const feedBtn = document.getElementById("feedAllBtn");

  feedBtn.addEventListener("click", () => {
    feedAll()
      .then(() => {
        console.log("🍖 All dinos fed");
        loadDinos();
      })
      .catch((err) => console.error("❌ Feed error:", err));
  });

  // Remove a dino
  const handleRemove = (name) => {
    const data = { name };
    removeDino(data)
      .then(() => {
        console.log(`Dino ${name} removed`);
        loadDinos();
      })
      .catch((err) => console.error("❌ Feed error:", err));
  };

  // Remove All Dinos
  const removeAllBtn = document.getElementById("removeAllBtn");

  removeAllBtn.addEventListener("click", () => {
    removeAllDino()
      .then(() => {
        console.log("🍖 All dinos removed");
        loadDinos();
      })
      .catch((err) => console.error("❌ Remove error:", err));
  });

  // ======================
  // INIT
  // ======================

  loadDinos();
  setInterval(() => {
    loadDinos();
  }, 2000);
});
