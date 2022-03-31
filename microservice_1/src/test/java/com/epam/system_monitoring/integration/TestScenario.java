package com.epam.system_monitoring.integration;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.MentorDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.ModuleStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class TestScenario {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testScenario() throws Exception {
        //Создаем ментора и сохраняем в БД.
        MentorDTO mentorDTO = new MentorDTO("mentor1", "mentor1", "mentorik1", "mentor@mail.ru", "123");
        mockMvc.perform(post("http://localhost:8080/api/mentor/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mentorDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(mentorDTO.getName())))
                .andExpect(jsonPath("$.surname", is(mentorDTO.getSurname())))
                .andExpect(jsonPath("$.username", is(mentorDTO.getUsername())))
                .andExpect(jsonPath("$.phoneNumber", is(mentorDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.email", is(mentorDTO.getEmail())))
                .andReturn();

        //Проверяем, что ментор с id = 1 есть в БД.
        mockMvc.perform(get("http://localhost:8080/api/mentor/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(mentorDTO.getName())))
                .andExpect(jsonPath("$.surname", is(mentorDTO.getSurname())))
                .andExpect(jsonPath("$.username", is(mentorDTO.getUsername())))
                .andExpect(jsonPath("$.phoneNumber", is(mentorDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.email", is(mentorDTO.getEmail())));

        //Создаем студента и сохраняем в БД.
        StudentDTO studentDTO = new StudentDTO("student1", "stud1", "studentik1");
        mockMvc.perform(post("http://localhost:8080/api/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(studentDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(studentDTO.getName())))
                .andExpect(jsonPath("$.surname", is(studentDTO.getSurname())))
                .andExpect(jsonPath("$.username", is(studentDTO.getUsername())))
                .andReturn();

        //Проверяем, что студент с id = 1 есть в БД.
        mockMvc.perform(get("http://localhost:8080/api/student/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(studentDTO.getName())))
                .andExpect(jsonPath("$.surname", is(studentDTO.getSurname())))
                .andExpect(jsonPath("$.username", is(studentDTO.getUsername())));

        //Закрепляем студента(id=1) за ментором(id=1) и проверяем.
        mockMvc.perform(post("http://localhost:8080/api/mentor/add/1/1"))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("http://localhost:8080/api/mentor/1/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(studentDTO.getName())))
                .andExpect(jsonPath("$[0].surname", is(studentDTO.getSurname())))
                .andExpect(jsonPath("$[0].username", is(studentDTO.getUsername())));

        //Создаем новый курс и сохранем в БД.
        CourseDTO courseDTO = new CourseDTO("Course 1");
        mockMvc.perform(post("http://localhost:8080/api/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(courseDTO.getTitle())))
                .andReturn();

        //Проверяем, что курс с id = 1 есть в БД.
        mockMvc.perform(get("http://localhost:8080/api/course/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(courseDTO.getTitle())));

        //Создаем новый модуль, у которого обязательно должен быть курс, ментор, статус и название.
        ModuleDTO moduleDTO = new ModuleDTO(
                1L,
                "Module 1",
                null,
                null,
                null,
                ModuleStatus.TODO,
                null,
                1L);

        mockMvc.perform(post("http://localhost:8080/api/module/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(moduleDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(moduleDTO.getTitle())))
                .andExpect(jsonPath("$.courseId", is(Math.toIntExact(moduleDTO.getCourseId()))))
                .andExpect(jsonPath("$.moduleStatus", is(String.valueOf(moduleDTO.getModuleStatus()))))
                .andExpect(jsonPath("$.reporterId", is(Math.toIntExact(moduleDTO.getReporterId()))))
                .andReturn();

        //Проверяем, что модуль с id = 1 есть в БД.
        mockMvc.perform(get("http://localhost:8080/api/module/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(moduleDTO.getTitle())))
                .andExpect(jsonPath("$.courseId", is(Math.toIntExact(moduleDTO.getCourseId()))))
                .andExpect(jsonPath("$.moduleStatus", is(String.valueOf(moduleDTO.getModuleStatus()))))
                .andExpect(jsonPath("$.reporterId", is(Math.toIntExact(moduleDTO.getReporterId()))));

        //Обновляем модуль(id=1) и сохраняем изменения в БД.
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime deadline = startDate.plus(1, ChronoUnit.DAYS);
        ModuleDTO updatedModuleDTO = new ModuleDTO(
                1L,
                "Module 1",
                "Lorem",
                null,
                deadline,
                ModuleStatus.INPROGRESS,
                1L,
                1L);

        mockMvc.perform(post("http://localhost:8080/api/module/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedModuleDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedModuleDTO.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedModuleDTO.getDescription())))
                .andExpect(jsonPath("$.courseId", is(Math.toIntExact(updatedModuleDTO.getCourseId()))))
                .andExpect(jsonPath("$.deadline", is(updatedModuleDTO.getDeadline().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andExpect(jsonPath("$.moduleStatus", is(String.valueOf(updatedModuleDTO.getModuleStatus()))))
                .andExpect(jsonPath("$.assigneeId", is(Math.toIntExact(updatedModuleDTO.getAssigneeId()))))
                .andExpect(jsonPath("$.reporterId", is(Math.toIntExact(updatedModuleDTO.getReporterId()))))
                .andReturn();

        //Проверяем, что у модуля(id=1) обновились данные.
        mockMvc.perform(get("http://localhost:8080/api/module/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedModuleDTO.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedModuleDTO.getDescription())))
                .andExpect(jsonPath("$.courseId", is(Math.toIntExact(updatedModuleDTO.getCourseId()))))
                .andExpect(jsonPath("$.deadline", is(updatedModuleDTO.getDeadline().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .andExpect(jsonPath("$.moduleStatus", is(String.valueOf(updatedModuleDTO.getModuleStatus()))))
                .andExpect(jsonPath("$.assigneeId", is(Math.toIntExact(updatedModuleDTO.getAssigneeId()))))
                .andExpect(jsonPath("$.reporterId", is(Math.toIntExact(updatedModuleDTO.getReporterId()))));

        //Проверяем, что у курса(id=1) есть список модулей, в котором лежит закрепленный за ним модуль(id=1).
        mockMvc.perform(get("http://localhost:8080/api/course/1/modules"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(updatedModuleDTO.getTitle())))
                .andExpect(jsonPath("$[0].reporterId", is(Math.toIntExact(updatedModuleDTO.getReporterId()))));

        //Проверяем, что у ментора(id=1) есть список модулей, в котором лежит закрепленный за ним модуль(id=1).
        mockMvc.perform(get("http://localhost:8080/api/mentor/1/modules"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(updatedModuleDTO.getTitle())))
                .andExpect(jsonPath("$[0].reporterId", is(Math.toIntExact(updatedModuleDTO.getReporterId()))));

        //Проверяем, что у студента(id=1) есть список модулей, в котором лежит закрепленный за ним модуль(id=1).
        mockMvc.perform(get("http://localhost:8080/api/student/1/modules"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(updatedModuleDTO.getTitle())))
                .andExpect(jsonPath("$[0].reporterId", is(Math.toIntExact(updatedModuleDTO.getReporterId()))));

        //Проверяем, что после удаления курса(id=1) удалится и его дочерний модуль(id=1).
        //Удаляем модуль.
        mockMvc.perform(post("http://localhost:8080/api/course/delete/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(String.format("Course with id: %d was deleted", 1))))
                .andReturn();

        //Получаем ошибку вместо модуля(id=1), т.к. теперь модуля с id=1 не существует в БД.
        mockMvc.perform(get("http://localhost:8080/api/module/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Module not found with id: 1")))
                .andReturn();

        //Проверяем, что у ментора(id=1) есть список модулей, но он пуст.
        mockMvc.perform(get("http://localhost:8080/api/mentor/1/modules"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));

        //Проверяем, что у студента(id=1) есть список модулей, но он пуст.
        mockMvc.perform(get("http://localhost:8080/api/student/1/modules"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
