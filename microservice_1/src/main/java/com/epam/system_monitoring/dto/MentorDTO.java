package com.epam.system_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDTO {

    @NotEmpty(message = "The name field cannot be empty!")
    private String name;
    @NotEmpty(message = "The surname field cannot be empty!")
    private String surname;

    private String username;

    @NotEmpty(message = "The email field cannot be empty!")
    @Email(message = "Email should be valid!")
    private String email;

    private String phoneNumber;
//    private List<StudentDTO> students;
//    private List<ModuleDTO> modules;
}
