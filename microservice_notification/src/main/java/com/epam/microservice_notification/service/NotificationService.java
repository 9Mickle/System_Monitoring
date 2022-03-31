package com.epam.microservice_notification.service;

import java.util.Map;

public interface NotificationService {

    void notify(Map<String, String> map);
}
