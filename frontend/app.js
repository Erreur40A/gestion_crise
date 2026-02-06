function render(dinos) {
  const div = document.getElementById('dinos');
  div.innerHTML = '';
  dinos.forEach(d => {
    div.innerHTML += `<p> <span class="${d.energy > 0 ? "" : "disabled"}">${d.name} - Energy: ${d.energy} - Hunger: ${d.hunger} - Danger: ${d.dangerLevel}</span> <span class="status">${d.hunger == 100 && d.energy > 0 ? "This dinosaur is very hungry !" : d.energy <= 0 ? "This dinosaur is dead.." : ""}</span></p>`;
  });
}
async function refresh() {
  try {
    const response = await getDinos();
    const dinos = await response.json();
    render(dinos);
  } catch (error) {
    console.error('Erreur:', error);
  }
}

async function handleAddDino() {
  try {
    // Récupérer la liste des espèces
    const speciesResponse = await getSpecies();
    const species = await speciesResponse.json();
    
    // Créer une liste d'options
    const speciesList = Object.keys(species).sort();
    const speciesOptions = speciesList.map((s, i) => `${i + 1}. ${s} (Danger: ${species[s]})`).join('\n');
    
    const choice = prompt(`Choisissez une espèce (entrez le numéro) :\n\n${speciesOptions}`);
    
    if (!choice) return;
    
    const index = parseInt(choice) - 1;
    if (index < 0 || index >= speciesList.length) {
      alert("Choix invalide !");
      return;
    }
    
    const espece = speciesList[index];
    const name = prompt(`Nom du dinosaure (espèce: ${espece}) ?`);
    
    if (!name) return;
    
    const newDino = {
      espece: espece,
      name: name,
      energy: 100,
      hunger: 0
    };
    
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

refresh();
setInterval(refresh, 2000);