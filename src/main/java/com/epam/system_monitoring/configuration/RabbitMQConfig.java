package com.epam.system_monitoring.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "microservice_queue";
    public static final String EXCHANGE = "myExchange";
    public static final String ROUTING_KEY = "myRoutKey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    //Обменник.
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    //Связка обменника и очереди через ключ маршрутизации.
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    //Конвертер сообщений.
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
