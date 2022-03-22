![library_image](https://cdn.icon-icons.com/icons2/2416/PNG/128/heart_list_task_to_do_icon_146658.png)
# Microservice - CRUD application

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
* Connect to PostgreSQL (db name = system_monitoring).
* Run application.
* RabbitMQ: docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management
* Clone this repository: https://github.com/9Mickle/microservice_notification and run application.

### *APIs:*
You can view the documentation for all endpoints at this url: http://localhost:8080/swagger-ui/index.html
