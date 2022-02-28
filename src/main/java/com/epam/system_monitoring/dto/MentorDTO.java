package com.epam.system_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDTO {

    @NotEmpty(message = "The name field cannot be empty!")
    private String name;
    @NotEmpty(message = "The surname field cannot be empty!")
    private String surname;
    @NotEmpty(message = "The username field cannot be empty!")
    private String username;
}
