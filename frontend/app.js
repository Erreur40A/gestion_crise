document.addEventListener('DOMContentLoaded', () => {

    const API_BASE = 'http://localhost:8080/api';
    const statusEl = document.getElementById('status');
    let statusTimer = null;

    function setStatus(type, message, timeoutMs = 3000) {
        if (!statusEl) return;
        statusEl.className = type;
        statusEl.textContent = message;
        statusEl.style.display = 'block';
        if (statusTimer) {
            clearTimeout(statusTimer);
        }
        if (timeoutMs) {
            statusTimer = setTimeout(() => {
                statusEl.style.display = 'none';
            }, timeoutMs);
        }
    }


    // API CALLS


    function getDinos() {
        return fetch(`${API_BASE}/dinos`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch dinos');
                }
                return response.json();
            });
    }

    function addDino(dino) {
        return fetch(`${API_BASE}/dinos`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dino)
        });
    }

    function feedAll() {
        return fetch(`${API_BASE}/feed`, {
            method: 'GET'
        });
    }


    // HELPERS

    function getDangerClass(level) {
        if (level <= 3) return 'low';
        if (level <= 6) return 'medium';
        return 'high';
    }


    // RENDER


    function render(dinos) {
        const container = document.getElementById('dino-container');

        if (!container) {
            console.error('❌ dino-container not found');
            return;
        }

        container.innerHTML = '';

        if (!Array.isArray(dinos) || dinos.length === 0) {
            container.innerHTML = '<p>No dinosaurs found 🦕</p>';
            return;
        }

        dinos.forEach(dino => {
            const card = document.createElement('div');
            card.className = 'dino-card';

            card.innerHTML = `
                <div class="dino-name">🦖 ${dino.name}</div>
                <div class="energy">⚡ Energy: ${dino.energy}</div>
                <div class="danger ${getDangerClass(dino.dangerLevel)}">
                    ☠️ Danger level: ${dino.dangerLevel}
                </div>
                <div>Status: ${dino.alive ? '✅ Alive' : '💀 Dead'}</div>
            `;

            container.appendChild(card);
        });
    }

    // ======================
    // LOAD / REFRESH
    // ======================

    function loadDinos() {
        getDinos()
            .then(dinos => {
                render(dinos);
            })
            .catch(error => {
                console.error('❌ API error:', error);
                setStatus('error', 'Impossible de charger les dinosaures.');
            });
    }

    // EVENTS

    // Add Dino
    const form = document.getElementById('addDinoForm');

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const name = document.getElementById('dinoName').value.trim();
        const energyValue = document.getElementById('dinoEnergy').value;
        const dangerValue = document.getElementById('dinoDanger').value;

        if (!name) {
            setStatus('error', 'Le nom du dinosaure est requis.');
            return;
        }

        const newDino = {
            name: name,
            ...(energyValue !== '' && { energy: Number(energyValue) }),
            ...(dangerValue !== '' && { dangerLevel: Number(dangerValue) })
        };

        addDino(newDino)
            .then(() => {
                form.reset();
                setStatus('success', 'Dinosaure ajouté.');
                loadDinos();
            })
            .catch(err => {
                console.error(' Add dino error:', err);
                setStatus('error', 'Erreur lors de l’ajout du dinosaure.');
            });
    });

    // Feed All
    const feedBtn = document.getElementById('feedAllBtn');

    feedBtn.addEventListener('click', () => {
        feedAll()
            .then(() => {
                setStatus('success', 'Tous les dinosaures ont été nourris.');
                loadDinos();
            })
            .catch(err => {
                console.error('❌ Feed error:', err);
                setStatus('error', 'Erreur pendant le nourrissage.');
            });
    });

    // ======================
    // INIT
    // ======================

    loadDinos();

});
