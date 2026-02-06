function render(dinos) {
  const div = document.getElementById('dinos');
  div.innerHTML = '';
  dinos.forEach(d => {
    div.innerHTML += `<p> <span class="${d.energy > 0 ? "" : "disabled"}">${d.name} - Energy: ${d.energy} - Hunger: ${d.hunger} - Danger: ${d.dangerLevel}</span> <span class="status">${d.hunger == 100 && d.energy > 0 ? "This dinosaur is very hungry !" : d.energy <= 0 ? "This dinosaur is dead.." : ""}</span></p>`;
  });
}
function refresh() {
  getDinos()
  .then(response => response.json())
  .then(dinos => render(dinos))
  .catch(error => console.error('Erreur:', error));
}

async function handleAddDino() {
  const name = prompt("Nom du dinosaure ?");
  if (!name) return;
  
  const newDino = {
    name: name,
    energy: 100,
    hunger: 0,
    dangerLevel: parseInt(prompt("Dangerosité du dinosaure ?"))
  };
  
  try {
    await addDino(newDino);
    refresh();
  } catch (error) {
    console.error('Erreur ajout dino:', error);
  }
}

async function handleFeedAll() {
  try {
    await feedDinos();
    refresh();
  } catch (error) {
    console.error('Erreur nourrir dinos:', error);
  }
}

refresh()
setInterval(refresh, 2000)

window.addEventListener("DOMContentLoaded", () => {
    const btns = document.querySelectorAll(".btn-container .btn");
    btns.forEach((btn, index) => {
        btn.addEventListener("click", () => {
            if(index === 0){
                // ADD
                handleAddDino();
            } else {
                // FEED
                handleFeedAll();
            }
        });
    })
})