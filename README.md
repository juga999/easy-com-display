Prérequis Développeur
=====================

- Maven 3
- Postgresql 9 minimum
- npm

Préparer l'application
======================

Database
--------

Il faut au préalable avoir une base de données `ecd` (sous postgresql donc) et l'utilisateur `ecd` ayant les droits dessus.
```
CREATE USER ecd WITH PASSWORD 'ecd';
CREATE DATABASE ecd OWNER ecd;
```

Il faut ensuite lancer les migrations de la base de données:
```
mvn flyway:migrate
```

Installation des dépendances du frontend
----------------------------------------

Dans le répertoire `src/main/web`, installer les dépendances décrites dans package.json.
```
npm install
```

Lancement de l'application
==========================

Frontend
--------

Dans le répertoire lancer la commande suivante:
```
npm run dev
```
Cela lancera un serveur sur le port 8080 pour le développement du frontend. Les requêtes au backend seront routées sur le port 8084.

Backend
-------

Pour constuire le backend et le frontend:
```
mvn package
```

Pour lancer l'application:
```
./target/ecd-app/bin/EasyComDisplay
```