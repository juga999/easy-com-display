Prérequis Développeur
=====================

- Maven 3
- Postgresql 9 minimum
- npm

Préparer l'application
======================

Vue CLI
-------
``̀
sudo npm install -g vue-cli
``̀

Database
--------

Il faut au préalable avoir une base de données `ecd` (sous postgresql donc) et l'utilisateur `ecd` ayant les droits dessus.
```
CREATE USER ecd WITH PASSWORD 'ecd';
CREATE DATABASE ecd OWNER ecd;
```

Il faut ensuite lancer le script contenu dans `src/main/resources/initDb.sql` pour initialiser la base de données.
```
psql -d ecd -U ecd -f {PATH_ECD}/src/main/resources/initDb.sql
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

Dans le répertoire lancer la commande suivate:
```
vue-build -c vue-cli.config.js ./src/index.js
```
Cela lancera un serveur sur le port 4000 pour le développement du frontend. Les requêtes au backend seront routées sur le port 8084.

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