package com.epam.system_monitoring.dto;

import com.epam.system_monitoring.entity.ModuleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {

    @NotNull(message = "The courseId field cannot be empty!")
    private Long courseId;

    @NotEmpty(message = "The title field cannot be empty!")
    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime deadline;

    @NotNull(message = "The status field cannot be empty!")
    private ModuleStatus moduleStatus;

    private Long assigneeId;

    @NotNull(message = "The reporter field cannot be empty!")
    private Long reporterId;
}
