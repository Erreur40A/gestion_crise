document.addEventListener('DOMContentLoaded', () => {

    const API_BASE = 'http://localhost:8080/api';


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
                console.log('✅ Dinos loaded:', dinos);
                render(dinos);
            })
            .catch(error => {
                console.error('❌ API error:', error);
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
            alert('Dino name is required');
            return;
        }

        const newDino = {
            name: name,
            ...(energyValue !== '' && { energy: Number(energyValue) }),
            ...(dangerValue !== '' && { dangerLevel: Number(dangerValue) })
        };

        addDino(newDino)
            .then(() => {
                console.log(' Dino added');
                form.reset();
                loadDinos();
            })
            .catch(err => console.error(' Add dino error:', err));
    });

    // Feed All
    const feedBtn = document.getElementById('feedAllBtn');

    feedBtn.addEventListener('click', () => {
        feedAll()
            .then(() => {
                console.log(' All dinos fed');
                loadDinos();
            })
            .catch(err => console.error('❌ Feed error:', err));
    });

    // ======================
    // INIT
    // ======================

    loadDinos();

});
