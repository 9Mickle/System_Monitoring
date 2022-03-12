package com.epam.system_monitoring.dto;

import com.epam.system_monitoring.annotation.CustomJsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CustomJsonRootName(plural = "students", singular = "student")
public class StudentDTO {

    @NotEmpty(message = "The name field cannot be empty!")
    private String name;
    @NotEmpty(message = "The surname field cannot be empty!")
    private String surname;
    private String username;
}
