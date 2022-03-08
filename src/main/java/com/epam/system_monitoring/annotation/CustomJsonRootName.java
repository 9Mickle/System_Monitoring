package com.epam.system_monitoring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Для того чтобы у json'а ДТО сущностей был корневой элемент.
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CustomJsonRootName {
    String singular(); // для одного объекта.
    String plural(); // для коллекции.
}
