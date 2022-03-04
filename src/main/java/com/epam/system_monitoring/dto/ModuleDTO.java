package com.epam.system_monitoring.dto;

import com.epam.system_monitoring.annotation.CustomJsonRootName;
import com.epam.system_monitoring.entity.ModuleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CustomJsonRootName(plural = "modules", singular = "module")
public class ModuleDTO {

    @NotNull(message = "The courseId field cannot be empty!")
    private Long courseId;
    @NotEmpty(message = "The title field cannot be empty!")
    private String title;
    private String description;
    private LocalTime time;
    //todo добавить ограничения.
    private ModuleStatus moduleStatus;
    private Long assigneeId;

    @NotNull(message = "The reporter field cannot be empty!")
    private Long reporterId;
}
