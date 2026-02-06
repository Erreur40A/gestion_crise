function getDinos() {
  return fetch('http://localhost:8080/api/dinos');
}

function addDino(dinoData) {
  return fetch('http://localhost:8080/api/dinos/add', {
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

function feedDinos() {
  return fetch('http://localhost:8080/api/dinos/feed', {
    method: 'POST'
  });
}