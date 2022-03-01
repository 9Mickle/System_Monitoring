package com.epam.system_monitoring.dto;

import com.epam.system_monitoring.annotation.CustomJsonRootName;
import com.epam.system_monitoring.entity.ModuleStatus;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CustomJsonRootName(plural = "modules", singular = "module")
public class ModuleDTO {

    @NotEmpty(message = "The title field cannot be empty!")
    private String title;
    private String description;
    //todo добавить ограничения.
    private LocalTime time;
    //todo добавить ограничения.
    private ModuleStatus moduleStatus;
}
