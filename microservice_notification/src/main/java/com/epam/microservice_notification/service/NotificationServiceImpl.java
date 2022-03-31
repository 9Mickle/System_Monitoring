package com.epam.microservice_notification.service;

import com.epam.microservice_notification.configuration.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Сервис, обеспечивающий доставку сообщений ментору о смене статуса модуля.
 * Здесь вместо отправки сообщений происходит логгирование.
 */
@Component
public class NotificationServiceImpl implements NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void notify(Map<String, String> map) {
       String email = map.get("email");
       map.putIfAbsent("phoneNumber","");
       String phoneNumber = map.get("phoneNumber");
       String moduleStatus = map.get("moduleStatus");

       if (!phoneNumber.equals("")) {
           logger.info(String.format("Send a message to: %s. The status of the module on: %s has changed", phoneNumber, moduleStatus));
       }
       logger.info(String.format("Send a message to: %s. The status of the module on: %s has changed", email, moduleStatus));
    }
}
