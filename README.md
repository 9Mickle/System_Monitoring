![library_image](https://cdn.icon-icons.com/icons2/3310/PNG/128/laptop_computer_books_study_pc_icon_209270.png)
# System_Monitoring - CRUD application

## 1. Project description:
This project is a management system for courses, modules, mentors and students.
* microservice_1 - responsible for CRUD operations on entities.
* microservice_notification - responsible for notifying the mentor about the progress of his students.

### 2. Technology stack:
* Java 11
* Spring 5.x.x, Spring Data JPA
* JUnit, Mockito
* Maven
* Docker
* PostgreSQL
* H2 for tests
* Liquibase
* Lombok
* RabbitMQ
* Mapstruct
* Spring doc

### How to run:
* Clone this repository.
* Cd in System_monitoring and 
* Run commands: 
  * mvn clean install -DskipTests=true
  * docker-compose build
  * docker-compose up

### *APIs:*
You can view the documentation for all endpoints at this url: http://localhost:8080/swagger-ui/index.html

### Postman
For clarity, you can use file System_monitoring.postman_collection
