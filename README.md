# Deploy aplikacije na web (Digital ocean VM)

## Stvaranje VM

1. Odi na digitalocean.com i stvori račun (moraš dodati karticu iako dobiješ 200$ kredita na 60 dana)
2. Odaberi Virtual Machine
3. Regiju postavi na Frankfurt
4. Postavi ime servera

## Postavljanje servera i postgresql baze

### Server
1. Ažuriraj server
`sudo apt upgrade`

2. Postavi dodatnu zaštitu od botova
```
sudo apt install fail2ban
systemctl restart fail2ban.service
systemctl status fail2ban.service
```

### Postgresql baza podataka
1. Instaliraj postgresql i pokreni ga kao servis
```
sudo apt-get install postgresql postgresql-contrib
sudo systemctl start postgresql.service
sudo systemctl enable postgresql.service
sudo systemctl status postgresql.service
```

2. Ulogiraj se u psql CLI
`sudo -u postgres psql`

3. Postavi korisnika i lozinku (ovo se koristi u application.properties u Springu)
`\password postgres`
*upiši lozinku*

4. Stvori bazu *cdb* (pazi na kapitalizaciju)
`CREATE DATABASE cdb`

5. Spoji se na bazu
`\c cdb`

6. Izađi iz psql CLI
`\q`

## Deploy Spring (gradle) backenda

### Lokalno

1. Pozicioniraj se u Backend folder
`cd Backend`

2. Stvori .jar datoteku (stvoriti će se u build/libs)
`gradle bootJar`

3. Prebaci .jar datoteku na server
`scp build/libs/backend-0.0.1-SNAPSHOT.jar root@159.65.127.217:/var/www`
*upiši lozinku*

### Server
Spoji se na server kao root
`ssh root@IPv4_adresa_servera`

1. Instaliraj javu
`apt install openjdk-17-jdk`

#### Da radi dok si logiran:
2. Pozicioniraj se u /var/www folder
`cd /var/www`

3. Pokreni .jar datoteku
`java -jar backend-0.0.1-SNAPSHOT.jar`

#### Da radi kontinuirano:
2. Pozicioniraj se u folder sa servisima
`cd /usr/lib/systemd/system`

3. Napravi novu datoteku (servis) naziva runSpringServer.service (ili nešto slično)
`nano runSpringServer.service`

4. Zalijepi sljedeći kod u datoteku
```
[Unit]
Description=webserver Daemon

[Service]
ExecStart=/usr/bin/java -jar /var/www/backend-0.0.1-SNAPSHOT.jar
User=root

[Install]
WantedBy=multi-user.target
```

5. Naredbe za upravljanje servisom (kao sudo)
```
systemctl start runSpringServer.service # starts the service
systemctl enable runSpringServer.service # auto starts the service
systemctl disable runSpringServer.service # stops autostart
systemctl stop runSpringServer.service # stops the service
systemctl restart runSpringServer.service # restarts the service
systemctl status runSpringServer.service # shows current service status (output)
```

Odspoji se sa servera
`logout`

### Browser
U URL browsera upiši IPv4_adresa_servera:8080