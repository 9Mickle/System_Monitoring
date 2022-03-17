package com.epam.system_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    @NotEmpty(message = "The title field cannot be empty!")
    @Size(min = 2, max = 30, message = "Title should be between 2 and 30 characters!")
    private String title;
//    private List<ModuleDTO> modules;
}
