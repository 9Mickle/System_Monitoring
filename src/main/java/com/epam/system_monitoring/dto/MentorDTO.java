package com.epam.system_monitoring.dto;

import com.epam.system_monitoring.annotation.CustomJsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CustomJsonRootName(plural = "mentors", singular = "mentor")
public class MentorDTO {

    @NotEmpty(message = "The name field cannot be empty!")
    private String name;
    @NotEmpty(message = "The surname field cannot be empty!")
    private String surname;
    @NotEmpty(message = "The username field cannot be empty!")
    private String username;
//    private List<StudentDTO> students;
//    private List<ModuleDTO> modules;
}
