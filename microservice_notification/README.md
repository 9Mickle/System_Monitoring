![library_image](https://cdn.icon-icons.com/icons2/1283/PNG/128/1497619898-jd24_85173.png)
# Microservice 2 - notification application

## Task descriptions:
Provides sending notifications. 
The notification can be sent to the mentor by different channels (email, sms)
Accordingly, the interface and its implementation should be added.
The interface must contain a single method - notify

In the context of the task, the actual message will not be sent.
Instead, you need to add an implementation that simply writes a message to the log.

### How to run:
* Clone this repository.
* Cd in System_monitoring and run command: mvn clean install -DskipTests=true
* Cd in microservice_notification and run commands:
  * docker-compose build
  * docker-compose up
