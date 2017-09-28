# EasyComDisplay - Self hosted and flexible digital signage solution for the RaspberryPi

EasyComDisplay is a web application for the display and management of information for small and medium organisations.
EasyComDisplay targets the RaspberryPi as its main platform, but other Linux environments can be used as well.

## Requirements

### Java 8
EasyComDisplay requires Java 8 to run.
For the RaspberryPi, the official Raspbian distribution ships with a Java SE environment.

### PostgreSQL
EasyComDisplay stores its metadata in a PostgreSQL database.

```
sudo apt-get install postgresql
```

Next, create the EasyComDisplay database:
```
sudo -u postgres psql postgres
CREATE USER ecd WITH PASSWORD 'ecd';
CREATE DATABASE ecd;
GRANT ALL PRIVILEGES ON DATABASE ecd TO ecd;
\q
```

### LibreOffice and Universal Office Converter (unoconv)
Those two applications are required to import presentations made with Microsoft Powerpoint or LibreOffice Impress.
For the RaspberryPi, the official Raspbian distribution already ships with LibreOffice.

EasyComDisplay 'talks' to LibreOffice through the unoconv (http://dag.wiee.rs/home-made/unoconv/) application.
To install unoconv on the RaspberryPi:
```
sudo apt-get install unoconv
```

Next, as root, install unoconv as a service declared in a unoconv.service file:
```
vim /etc/systemd/system/unoconv.service
```

```
[Unit]
Description=Unoconv listener for document conversions
Documentation=https://github.com/dagwieers/unoconv
After=network.target remote-fs.target nss-lookup.target

[Service]
Type=simple
Environment="UNO_PATH=/usr/lib/libreoffice/program"
ExecStart=/usr/local/bin/unoconv --listener

[Install]
WantedBy=multi-user.target
```
```
systemctl enable unoconv.service
systemctl start unoconv.service
```

### ImageMagick
ImageMagick (https://www.imagemagick.org) is used to convert and resize the images used for your information stream.

## Installation

TODO

# For the developer

## Requirements

- Maven 3
- npm

## Prepare the database

See the requirements above for the PostgreSQL database configuration.

Type the following command to create the ecd_app database schema:
```
mvn flyway:migrate
```

## Frontend development

In the `src/main/web` directory, type the following command.
```
npm install
```

You can then start the development server that will listen on port 808O:
```
npm run dev
```
The frontend expects to find the backend on the port 8084.

## Backend development

To package the application:
```
mvn package
```

To launch the application:
```
./target/ecd-app/bin/EasyComDisplay
```

# TODOs
- Detail the installation procedure
- Translate the application
- Add acccess controls