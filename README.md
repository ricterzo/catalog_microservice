user-microservice
============

### Deploy in Docker

    docker:publishLocal

### Run in kubernates

Avviare il microservizio su kubernates

    kubectl run users-microservice --image=users-microservice:0.1 -- -Dconfig.resource=application-prod.conf

Esporre il microservizio all'esterno
 
    kubectl expose deployment users-microservice --name=users-microservice-service --port=9000 --type=LoadBalancer
    
Update di versione

    kubectl set image deployment/users-microservice users-microservice=users-microservice:{versione}