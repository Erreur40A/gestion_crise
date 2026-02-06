function render(dinos) {
  const div = document.getElementById('dinos');
  div.innerHTML = '';
  dinos.forEach(d => {
    div.innerHTML += `<p>${d.name} - Energy: ${d.energy} - Danger: ${d.dangerLevel}</p>`;
  });
}
function refresh() {
  getDinos()
  .then(response => response.json())
  .then(dinos => render(dinos))
  .catch(error => console.error('Erreur:', error));
}

refresh()
setInterval(refresh, 2000)