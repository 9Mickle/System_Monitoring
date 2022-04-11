package com.epam.system_monitoring.integration.controller.course.post;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.service.impl.CourseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseServiceImpl courseService;

    private static CourseDTO courseDTO;

    @BeforeEach
    public void setUp() {
        courseDTO = new CourseDTO("Course 1");
        courseService.saveCourse(courseDTO); //create.
    }

    /**
     * Если получаем ошибку TitleAlreadyExist, то значит курс с таким названием уже сохранен и создан.
     */
    @Test
    public void shouldThrowTheErrorTitleAlreadyExistWhenCreateCourse() throws Exception {
        mockMvc.perform(post("http://localhost:8080/api/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("A course with this title already exists")))
                .andReturn();
    }

    /**
     * Обновление старого названия курса на новое.
     */
    @Test
    public void shouldUpdateCourse() throws Exception {
        long courseId = 1L;
        String newTitle = "newTitle";
        CourseDTO courseDTO = new CourseDTO(newTitle);

        mockMvc.perform(put("http://localhost:8080/api/course/update/" + courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(newTitle)))
                .andReturn();
    }

    /**
     * Если получаем TitleAlreadyExist при обновлении курса, значит курс с таким названием уже существует.
     */
    @Test
    public void shouldThrowTheErrorTitleAlreadyExistWhenUpdateCourse() throws Exception {
        long courseId = 1L;
        String sameTitle = "Course 1";
        CourseDTO updatedCourseDTO = new CourseDTO(sameTitle);

        mockMvc.perform(put("http://localhost:8080/api/course/update/" + courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCourseDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("A course with that title already exists!")))
                .andReturn();
    }

    @Test
    public void shouldDeleteCourse() throws Exception {
        long courseId = 1L;
        mockMvc.perform(delete("http://localhost:8080/api/course/delete/" + courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(String.format("Course with id: %d was deleted", courseId))))
                .andReturn();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}