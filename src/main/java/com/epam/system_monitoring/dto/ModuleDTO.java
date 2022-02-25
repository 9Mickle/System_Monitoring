package com.epam.system_monitoring.dto;

import com.epam.system_monitoring.entity.Mentor;
import com.epam.system_monitoring.entity.ModuleStatus;
import com.epam.system_monitoring.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {

    @NotEmpty(message = "The title field cannot be empty!")
    private String title;
    private String description;
    private String color;
    private LocalTime time;
    private ModuleStatus moduleStatus;

//    @NotEmpty(message = "The assignee field cannot be empty!")
    private Student assignee;
//    @NotEmpty(message = "The reporter field cannot be empty!")
    private Mentor reporter;
}
