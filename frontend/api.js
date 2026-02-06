const API_BASE = 'http://localhost:8080';

// ======================
// DINOSAURS
// ======================

export function getDinos() {
    return fetch(`${API_BASE}/api/dinos`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch dinosaurs');
            }
            return response.json();
        });
}

export function addDino(dinoData) {
    return fetch(`${API_BASE}/api/dinos`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dinoData)
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to add dinosaur');
        }
        return response.json();
    });
}

export function removeDino(dinoData) {
    return fetch(`${API_BASE}/api/dinos/remove`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dinoData)
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to remove dinosaur');
        }
        return response.json();
    });
}

export function removeAllDino() {
    return fetch(`${API_BASE}/api/dinos/removeAll`, {
        method: 'POST'
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to remove all dinosaurs');
        }
    });
}

export function feedDinos() {
    return fetch(`${API_BASE}/api/dinos/feed`, {
        method: 'POST'
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to feed dinosaurs');
        }
    });
}

// ======================
// SPECIES
// ======================

export function getSpecies() {
    return fetch(`${API_BASE}/api/species`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch species');
            }
            return response.json();
        });
}
