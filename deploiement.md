# Guide de Déploiement - DinoPark (Backend Java + Frontend)

Ce guide détaille les étapes pour déployer le projet DinoPark sur un serveur/VPS Debian ou sur votre machine locale Linux (Debian de préférence).

## Architecture du Projet

- **Backend** : Application Java (fichier .jar) qui s'exécute sur le port 8080
- **Frontend** : Application web statique servie par Nginx
- **Reverse Proxy** : Nginx pour HTTPS et proxy vers le backend

---

## Table des Matières

1. [Déploiement Local (Machine Debian)](#déploiement-local-machine-debian)
2. [Déploiement sur Serveur/VPS](#déploiement-sur-serveurvps)

---

## Déploiement Local (Machine Debian)

### 1. Récupération du Projet

Récupérez le dossier du projet corrigé et mis à jour depuis Google Drive :

```bash
# Téléchargez le dossier depuis le lien Drive fourni
# [lienDrive]

# Extrayez l'archive si nécessaire
unzip dinoPark.zip
cd dinoPark
```

### 2. Installation de Java (JRE/JDK)

Le backend nécessite Java pour exécuter le fichier .jar :

```bash
# Mise à jour des paquets
sudo apt update

# Installation de Java (OpenJDK 17 recommandé)
sudo apt install -y openjdk-17-jre-headless

# Vérification de l'installation
java -version
```

Si vous avez besoin du JDK complet pour compiler :
```bash
sudo apt install -y openjdk-17-jdk
```

### 3. Lancement du Backend

Le fichier .jar se trouve dans le dossier du projet. Lancez-le :

```bash
# Naviguez vers le dossier contenant le .jar
cd /chemin/vers/dinoPark/backend

# Exécution du .jar (il écoute sur localhost:8080)
java -jar dinoPark-backend.jar
```

**Pour exécuter en arrière-plan avec nohup :**
```bash
nohup java -jar dinoPark-backend.jar > backend.log 2>&1 &
```

**Ou créer un service systemd (recommandé pour production) :**

Créez le fichier `/etc/systemd/system/dinopark.service` :

```bash
sudo vim /etc/systemd/system/dinopark.service
```

Contenu du fichier :
```ini
[Unit]
Description=DinoPark Backend Service
After=network.target

[Service]
Type=simple
User=votre-utilisateur
WorkingDirectory=/chemin/vers/dinoPark/backend
ExecStart=/usr/bin/java -jar /chemin/vers/dinoPark/backend/dinoPark-backend.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Activez et démarrez le service :
```bash
sudo systemctl daemon-reload
sudo systemctl enable dinopark
sudo systemctl start dinopark
sudo systemctl status dinopark
```

### 4. Installation et Configuration de Nginx

#### 4.1 Installation de Nginx

```bash
sudo apt install -y nginx
```

#### 4.2 Création des Répertoires pour le Frontend

```bash
# Création du répertoire pour le frontend
sudo mkdir -p /usr/local/www/dinoPark/dist
sudo chown $USER /usr/local/www/dinoPark/dist
sudo chmod a+rx /usr/local/www/dinoPark

# Copie des fichiers du frontend
cp -r /chemin/vers/dinoPark/frontend/* /usr/local/www/dinoPark/dist/
```

#### 4.3 Génération du Certificat SSL Auto-signé (Déploiement Local)

Créez les répertoires pour les certificats :

```bash
sudo mkdir -p /etc/nginx/ssl/cert
sudo mkdir -p /etc/nginx/ssl/private
sudo chmod 700 /etc/nginx/ssl/private
```

Créez un script pour générer le certificat :

```bash
vim generate_cert.sh
```

Contenu du script :
```bash
#!/bin/sh

# Exit on error
set -o errexit

key_file_path=/etc/nginx/ssl/private/local.test.key
cert_file_path=/etc/nginx/ssl/cert/local.test.crt

# -nodes option to not protect the private key with a passphrase
# -x509 option tells req to create a self-signed certificate
sudo openssl req \
        -newkey rsa:2048 -nodes -keyout $key_file_path \
        -x509 -days 1200 -out $cert_file_path

# Set proper permissions
sudo chmod 600 $key_file_path
sudo chmod 644 $cert_file_path
```

Exécutez le script et répondez aux questions :
```bash
chmod +x generate_cert.sh
./generate_cert.sh
```

Réponses suggérées :
```
Country Name (2 letter code) [AU]: FR
State or Province Name (full name) [Some-State]: .
Locality Name (eg, city) []: Paris
Organization Name (eg, company) [Internet Widgits Pty Ltd]: Local Test
Organizational Unit Name (eg, section) []: 
Common Name (e.g. server FQDN or YOUR name) []: *.local.test
Email Address []: root@localhost
```

#### 4.4 Configuration de Nginx

Créez le fichier de configuration :

```bash
sudo vim /etc/nginx/sites-available/web-3.local.test.conf
```

Contenu du fichier :
```nginx
server {
    server_name web-3.local.test;
    listen 80;
    return 301 https://$host$request_uri;
}

server {
    server_name web-3.local.test;
    listen 443 ssl;
    ssl_certificate /etc/nginx/ssl/cert/local.test.crt;
    ssl_certificate_key /etc/nginx/ssl/private/local.test.key;

    access_log /var/log/nginx/web-3.local.test-access.log;
    error_log /var/log/nginx/web-3.local.test-error.log;

    root /usr/local/www/dinoPark/dist;

    location /api/ { # trailing slash required
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header Host $http_host;
      proxy_set_header X-NginX-Proxy true;

      # Port du backend Java
      proxy_pass http://127.0.0.1:8080/api/; # trailing slash required
      proxy_redirect off;
    }
}
```

Créez le lien symbolique et activez le site :
```bash
sudo ln -s /etc/nginx/sites-available/web-3.local.test.conf /etc/nginx/sites-enabled/web-3.local.test.conf
```

Testez la configuration Nginx :
```bash
sudo nginx -t
```

Si tout est OK, rechargez Nginx :
```bash
sudo systemctl reload nginx
sudo systemctl status nginx
```

#### 4.5 Configuration du fichier /etc/hosts

Ajoutez le nom de domaine local :

```bash
sudo vim /etc/hosts
```

Ajoutez à la fin du fichier :
```
127.0.0.1       web-3.local.test
```

### 5. Accès à l'Application

Ouvrez votre navigateur et accédez à :
```
https://web-3.local.test
```

**Note** : Comme le certificat est auto-signé, votre navigateur affichera un avertissement de sécurité. Cliquez sur "Avancé" puis "Accepter le risque et continuer" (Firefox) ou "Continuer vers le site" (Chrome).

### 6. Dépannage

#### Vérifier que le backend fonctionne :
```bash
curl http://localhost:8080/api/
```

#### Consulter les logs Nginx :
```bash
sudo tail -f /var/log/nginx/web-3.local.test-access.log
sudo tail -f /var/log/nginx/web-3.local.test-error.log
```

#### Consulter les logs du backend :
```bash
# Si lancé avec systemd
sudo journalctl -u dinopark -f

# Si lancé avec nohup
tail -f backend.log
```

#### Vérifier le statut des services :
```bash
sudo systemctl status nginx
sudo systemctl status dinopark
```

---

## Déploiement sur Serveur/VPS

### Prérequis

- Un serveur/VPS Debian 11, 12 ou 13
- Accès SSH avec privilèges sudo
- Un nom de domaine pointant vers votre serveur
- Ports 80 et 443 ouverts dans le pare-feu

### 1. Configuration DNS

Avant de commencer, configurez votre nom de domaine :

#### 1.1 Trouver l'IP de votre serveur

```bash
# Sur votre serveur
curl ifconfig.me
# ou
ip addr show
```

#### 1.2 Configurer les enregistrements DNS

Connectez-vous à votre fournisseur de nom de domaine (OVH, Gandi, Cloudflare, etc.) et ajoutez un enregistrement A :

```
Type: A
Nom: @ (ou votre sous-domaine, ex: dinopark)
Valeur: [IP_DE_VOTRE_SERVEUR]
TTL: 3600 (ou automatique)
```

Exemple pour un sous-domaine :
```
Type: A
Nom: dinopark
Valeur: 123.45.67.89
```

Votre site sera accessible via `dinopark.votre-domaine.com`

### 2. Connexion au Serveur

```bash
ssh utilisateur@votre-serveur.com
# ou
ssh utilisateur@123.45.67.89
```

### 3. Mise à Jour du Système

```bash
sudo apt update
sudo apt upgrade -y
```

### 4. Upload du Projet sur le Serveur

Depuis votre machine locale, uploadez le projet :

```bash
# Avec SCP
scp -r /chemin/vers/dinoPark utilisateur@votre-serveur.com:/home/utilisateur/

# Ou avec rsync (recommandé)
rsync -avz /chemin/vers/dinoPark utilisateur@votre-serveur.com:/home/utilisateur/
```

### 5. Installation de Java

```bash
sudo apt update
sudo apt install -y openjdk-17-jre-headless

# Vérification
java -version
```

### 6. Configuration du Backend comme Service

Créez le service systemd :

```bash
sudo vim /etc/systemd/system/dinopark.service
```

Contenu :
```ini
[Unit]
Description=DinoPark Backend Service
After=network.target

[Service]
Type=simple
User=votre-utilisateur
WorkingDirectory=/home/votre-utilisateur/dinoPark/backend
ExecStart=/usr/bin/java -jar /home/votre-utilisateur/dinoPark/backend/dinoPark-backend.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Activez et démarrez le service :
```bash
sudo systemctl daemon-reload
sudo systemctl enable dinopark
sudo systemctl start dinopark
sudo systemctl status dinopark
```

### 7. Installation de Nginx

```bash
sudo apt install -y nginx
```

### 8. Préparation du Frontend

```bash
sudo mkdir -p /usr/local/www/dinoPark/dist
sudo chown $USER /usr/local/www/dinoPark/dist
sudo chmod a+rx /usr/local/www/dinoPark

# Copie des fichiers du frontend
cp -r /home/votre-utilisateur/dinoPark/frontend/* /usr/local/www/dinoPark/dist/
```

### 9. Installation de Certbot (Let's Encrypt)

```bash
# Installation de Certbot et du plugin Nginx
sudo apt install -y certbot python3-certbot-nginx
```

### 10. Configuration Nginx (Sans SSL d'abord)

Créez une configuration temporaire sans SSL :

```bash
sudo vim /etc/nginx/sites-available/dinopark.conf
```

Contenu :
```nginx
server {
    server_name votre-domaine.com;  # Remplacez par votre domaine
    listen 80;

    root /usr/local/www/dinoPark/dist;
    index index.html;

    location /api/ {
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header Host $http_host;
      proxy_set_header X-NginX-Proxy true;

      proxy_pass http://127.0.0.1:8080/api/;
      proxy_redirect off;
    }

    location / {
      try_files $uri $uri/ /index.html;
    }
}
```

Activez le site :
```bash
sudo ln -s /etc/nginx/sites-available/dinopark.conf /etc/nginx/sites-enabled/dinopark.conf
sudo nginx -t
sudo systemctl reload nginx
```

### 11. Obtention du Certificat SSL avec Certbot

```bash
sudo certbot --nginx -d votre-domaine.com
```

Suivez les instructions interactives :
- Entrez votre email
- Acceptez les conditions
- Choisissez de rediriger HTTP vers HTTPS (option 2 recommandée)

Certbot modifiera automatiquement votre configuration Nginx pour ajouter SSL.

**Configuration finale générée par Certbot** :
```nginx
server {
    server_name votre-domaine.com;
    listen 80;
    return 301 https://$host$request_uri;
}

server {
    server_name votre-domaine.com;
    listen 443 ssl;
    
    ssl_certificate /etc/letsencrypt/live/votre-domaine.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/votre-domaine.com/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    root /usr/local/www/dinoPark/dist;
    index index.html;

    location /api/ {
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header Host $http_host;
      proxy_set_header X-NginX-Proxy true;

      proxy_pass http://127.0.0.1:8080/api/;
      proxy_redirect off;
    }

    location / {
      try_files $uri $uri/ /index.html;
    }
}
```

### 12. Renouvellement Automatique du Certificat

Les certificats Let's Encrypt sont valides 90 jours. Certbot configure automatiquement le renouvellement.

Testez le renouvellement :
```bash
sudo certbot renew --dry-run
```

### 13. Configuration du Pare-feu (UFW)

```bash
# Installation de UFW si nécessaire
sudo apt install -y ufw

# Autoriser SSH (important !)
sudo ufw allow ssh
sudo ufw allow 22/tcp

# Autoriser HTTP et HTTPS
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# Activer le pare-feu
sudo ufw enable
sudo ufw status
```

### 14. Accès à l'Application

Ouvrez votre navigateur et accédez à :
```
https://votre-domaine.com
```

---

## Vérifications et Dépannage

### Vérifier que le backend fonctionne
```bash
curl http://localhost:8080/api/
```

### Consulter les logs
```bash
# Logs Nginx
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log

# Logs Backend
sudo journalctl -u dinopark -f

# Tous les logs Nginx en temps réel
sudo tail -f /var/log/nginx/*
```

### Vérifier les services
```bash
sudo systemctl status nginx
sudo systemctl status dinopark
```

### Tester la configuration Nginx
```bash
sudo nginx -t
```

### Redémarrer les services
```bash
sudo systemctl restart nginx
sudo systemctl restart dinopark
```

---

## Notes Importantes

### Différences entre Déploiement Local et Serveur

| Aspect | Local | Serveur/VPS |
|--------|-------|-------------|
| Certificat SSL | Auto-signé (avertissement navigateur) | Let's Encrypt (certifié) |
| Nom de domaine | `*.local.test` dans `/etc/hosts` | Vrai domaine avec DNS |
| Accès | Uniquement depuis votre machine | Accessible publiquement |
| Sécurité | Pare-feu optionnel | Pare-feu obligatoire |

### Sécurité

- Changez les mots de passe par défaut
- Désactivez l'accès SSH par mot de passe (utilisez des clés SSH)
- Gardez votre système à jour
- Surveillez les logs régulièrement
- Configurez fail2ban pour bloquer les tentatives d'intrusion

### Maintenance

- Les certificats Let's Encrypt se renouvellent automatiquement
- Mettez à jour régulièrement : `sudo apt update && sudo apt upgrade`
- Sauvegardez régulièrement votre base de données et vos fichiers

---

## Ressources Utiles

- [Documentation Nginx](https://nginx.org/en/docs/)
- [Certbot Documentation](https://certbot.eff.org/)
- [Let's Encrypt](https://letsencrypt.org/)
- [OpenJDK](https://openjdk.org/)

---

**Projet DinoPark - Guide de Déploiement v1.0**
