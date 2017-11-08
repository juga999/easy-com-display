#!/bin/bash

# Script to install the EasyComDisplay application

## PostgreSQL
apt-get install postgresql

cat <<EOF >/tmp/ecd-initdb.sql
CREATE USER ecd WITH PASSWORD 'ecd';
CREATE DATABASE ecd;
GRANT ALL PRIVILEGES ON DATABASE ecd TO ecd;
EOF

su - postgres -c "psql postgres -f /tmp/ecd-initdb.sql"
rm /tmp/ecd-initdb.sql

## Unoconv
apt-get install unoconv

cat <<EOF >/etc/systemd/system/unoconv.service
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
EOF

systemctl enable unoconv.service
systemctl start unoconv.service

## ImageMagick
apt-get install imagemagick

cd /opt
wget https://github.com/juga999/easy-com-display/releases/download/v1.0/ecd-app-v1.0.tgz

tar xvzf ecd-app-v1.0.tgz

useradd -mU ecd
chown ecd:ecd -R /opt/ecd-app

cat <<EOF >/etc/systemd/system/ecd.service
[Unit]
Description=Easy Com Display
Documentation=https://github.com/juga999/easy-com-display
After=network.target remote-fs.target nss-lookup.target

[Service]
Type=simple
ExecStart=/opt/ecd-app/bin/EasyComDisplay
User=ecd
WorkingDirectory=/home/ecd
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

systemctl enable ecd.service
systemctl start ecd.service

