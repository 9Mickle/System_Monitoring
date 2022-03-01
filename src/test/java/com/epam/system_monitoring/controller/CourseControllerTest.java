package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.CourseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnAllCoursesInJSON() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/course/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses", hasSize(1)))
                .andExpect(jsonPath("$.courses[0].title", is("Course 1")));
    }

    @Test
    public void shouldReturnCourseByIdInJSON() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/course/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course.title", is("Course 1")));
    }

    @Test
    public void shouldReturnCourseByTitleInJSON() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/course/title/Course 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course.title", is("Course 1")));
    }

    @Test
    public void shouldReturnAllModulesInCourseInJSON() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/course/1/modules"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modules").isArray())
                .andExpect(jsonPath("$.modules", hasSize(3)));
    }

    //todo asJsonString

    @Test
    public void shouldCreateNewCourse() throws Exception{
        mockMvc.perform(post("http://localhost:8080/api/course/1/modules")
                .content(asJsonString(new CourseDTO("TestTitle", List.of()))));
    }

}