function getDinos() {
  return fetch(`${API_BASE}/api/dinos`);
}

function getSpecies() {
  return fetch(`${API_BASE}/api/species`);
}

function addDino(dinoData) {
  return fetch(`${API_BASE}/api/dinos`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(dinoData)
  });
}

function removeDino(dinoData) {
  return fetch(`${API_BASE}/api/dinos/remove`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(dinoData)
  });
}

function removeAllDino() {
  return fetch(`${API_BASE}/api/dinos/removeAll`, {
    method: 'POST',
  });
}

function feedDinos() {
  return fetch(`${API_BASE}/api/dinos/feed`, {
    method: 'POST'
  });
}