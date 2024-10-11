# Agricultural Research Data Index Tool

# Run environment
It is not necessary to install all the requirements because the project comes with a docker-compose.yml file that starts all the required services.

This docker-compose.yml allows the user to run the whole modules of the application with the use of docker and docker compose.
``` console
agricore@agricore-indexer:~$ sudo docker-compose up 
```

Endpoints:
- ARDIT (Frontend): localhost:4200
- ARDIT API (Backend): localhost:8080/api/
- ARDIT (LDAP): localhost:8081


# Development environment
It is not necessary to install all the requirements because the project comes with a docker-compose.dev.yml file that starts all the required services in Live Reload (Hot Swap) mode.

This docker-compose.dev.yml allows the developer to modify in real time all the source code of the services, displaying the changes in real time in the browser and in the console:
``` console
agricore@agricore-indexer:~$ sudo docker-compose -f docker-compose.dev.yml up    # Start hot swap services
agricore@agricore-indexer:~$ sudo docker-compose -f docker-compose.dev.yml down  # Stop hot swap services
```

Then run backend and frontend to start development.
``` console
agricore@agricore-indexer\frontend-angular:~$ npm start
backend: run on preferred IDE
```

Endpoints:
- ARDIT (Frontend): localhost:4200
- ARDIT API (Backend): localhost:8080/api/
- ARDIT (LDAP): localhost:8081

# ARDIT default users
    User: user1, Password: user1
    User: user2, Password: user2
    User: editor1, Password: editor1
    User: mantainer1, Password: mantainer1
    User: admin1, Password: admin1

## Backend-spring
- Java OpenSDK 11
- Maven

## Frontend-angular
- Node.js version 12
``` console
agricore@agricore-indexer:~$ sudo curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -
agricore@agricore-indexer:~$ sudo apt install -y nodejs
```
- @angular/cli via npm
``` console
agricore@agricore-indexer:~$ sudo npm install -g @angular/cli
```

## LDAP service
Agricore Indexer provide a development service called *phpldapadmin* which is used to manage the main LDAP service.

In order to access it, go to localhost:8081 and use the following credentials:
```
    Login DN: cn=admin, dc=agricore, dc=eu
    Password: admin
```
# Ontology

## Prerequisites

* Java OpenSDK 8
* [Protégé](https://protege.stanford.edu/)

## Structure

AGRICORE Ontology referers to many vocabularies that is used to define classes and properties.   
The main file is ```ontology-dcatap2-rdf/root-ontology.rdf```, open it with Protégé.