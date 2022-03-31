![library_image](https://cdn.icon-icons.com/icons2/2416/PNG/128/heart_list_task_to_do_icon_146658.png)
# Microservice 1 - CRUD application

## Task description:
### 1 Create a backend application that allows:

* CRUD for Course entity (Title)
* CRUD for Module entity (Course, title, description, start date, deadline, status, assignee, reporter)
* CRUD for Mentor entity (Username, name, surname, email, phone number)
* CRUD for Student entity (Username, name, surname)
* The microservice must contain an external API for processing requests.

### 2 Technology stack:
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
* Cd in System_monitoring and run command: mvn clean install -DskipTests=true
* Cd in microservice_1 and run commands:
  * docker-compose build
  * docker-compose up

### *APIs:*
You can view the documentation for all endpoints at this url: http://localhost:8080/swagger-ui/index.html
