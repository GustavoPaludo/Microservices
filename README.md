# Microservices
A microserviced server, with and authentication service, an eureka server and two services, the client and product microservices. This projects is a practical exercise in microservices development.

All access to train services must be authenticated. A service only accepts external communications if the request's JWT token is valid. The only open end-point is in the authentication service, through which web requests can reach other services as needed. Any other direct connection to the services will not be authorized.

The Eureka Server will be visible at the port 8761 on your localhost. 
To start the applications with the database connections, was added a docker-compose to the project, it will create the empty databases with the connections configurations for eatch service. To start the docker container with the three databases, run "docker-compose up" on the folder that contains the docker-compose.yml, it will setup and start the connections:

![image](https://github.com/user-attachments/assets/9bb235a5-a65a-4764-9945-9af86d8d4822)
