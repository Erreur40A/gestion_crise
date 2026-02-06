const images = [
  "OIP-182868429.jpeg",
  "OIP-651748097.jpeg",
  "OIP-1223847653.jpeg",
  "OIP-2945176161.jpeg",
  "OIP-3388326009.jpeg",
  "OIP-4006191225.jpeg"
]

function render(dinos) {
  const div = document.getElementById("dinos");
  div.innerHTML = "";
  dinos.forEach((d, idx) => {
    // const choice = Math.floor((Math.random()*images.length) % images.length); 
    const choice = idx;
    div.innerHTML += `<div class="card shake ${d.energy <= 0 ? "disabled" : ""}">
      <div class="card-header">
        <div class="remove-btn" onclick="handleRemoveDino('${d.name}')" data-name="${d.name}">
          <i class="hgi hgi-stroke hgi-delete-02"></i>
        </div>
        <div class="card-img">
            <img src="images/${images[choice]}" alt="${d.name} Picture" title="${d.name}" class="img-fluid">
        </div>
      </div>
      <div class="card-body">
        <h3 class="">Name : ${d.name}</h3>
        <h3 class="">Energy : ${d.energy}</h3>
        <h3 class="">Dangerousity Level : ${d.dangerLevel}</h3>
      </div>
    </div>`;
  });
}

const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("visible");
      }
    });
  },
  {
    root: document.querySelector("body"),
    rootMargin: "0px",
    scrollMargin: "0px",
    threshold: 0.3,
  },
);

function refresh() {
  getDinos()
    .then((response) => response.json())
    .then((dinos) => render(dinos))
    .catch((error) => console.error("Erreur:", error));
}

async function handleAddDino() {
  const name = prompt("Nom du dinosaure ?");
  if (!name) return;

  const newDino = {
    name: name,
    energy: 100,
    hunger: 0,
    dangerLevel: parseInt(prompt("Dangerosité du dinosaure ?")),
  };

  try {
    await addDino(newDino);
    refresh();
  } catch (error) {
    console.error("Erreur ajout dino:", error);
  }
}

async function handleRemoveDino(name) {

  const Dino = {
    name: name
  };

  try {
    await removeDino(Dino);
    refresh();
  } catch (error) {
    console.error("Erreur de suppression du dino:", error);
  }
}

async function handleFeedAll() {
  try {
    await feedDinos();
    refresh();
  } catch (error) {
    console.error("Erreur nourrir dinos:", error);
  }
}

refresh();
setInterval(refresh, 5000);

window.addEventListener("DOMContentLoaded", () => {
  const btns = document.querySelectorAll(".btn-container .btn");
  btns.forEach((btn, index) => {
    btn.addEventListener("click", () => {
      if (index === 0) {
        handleAddDino();
      } else {
        handleFeedAll();
      }
    });
  });

  const cards = document.querySelectorAll(".card");
  cards.forEach(c => observer.observe(c));
});
