function getDinos() {
  return fetch('http://localhost:8080/api/dinos');
}

function getSpecies() {
  return fetch('http://localhost:8080/api/species');
}

function addDino(dinoData) {
  return fetch('http://localhost:8080/api/dinos', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(dinoData)
  });
}

function removeDino(dinoData) {
  return fetch('http://localhost:8080/api/dinos/remove', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(dinoData)
  });
}

function removeAllDino() {
  return fetch('http://localhost:8080/api/dinos/removeAll', {
    method: 'POST',
  });
}

function feedDinos() {
  return fetch('http://localhost:8080/api/dinos/feed', {
    method: 'POST'
  });
}