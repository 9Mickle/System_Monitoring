package com.epam.system_monitoring.integration.course.get;

import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GetRequestTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        List<Module> moduleList = Arrays.asList(
                new Module(1L, "Module 1", null, null, null, null, null, null, null),
                new Module(2L, "Module 2", null, null, null, null, null, null, null)
        );
        Course course = new Course(1L, "Course 1", moduleList);

        when(courseRepository.findByTitle("Course 1")).thenReturn(Optional.of(course));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnAllCourses() throws Exception {
        List<Course> courseList = Arrays.asList(
                new Course(1L, "Course 1", List.of()),
                new Course(2L, "Course 2", List.of()));

        when(courseRepository.findAll()).thenReturn(courseList);
        mockMvc.perform(get("http://localhost:8080/api/course/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses", hasSize(2)))
                .andExpect(jsonPath("$.courses[0].title", is("Course 1")))
                .andExpect(jsonPath("$.courses[1].title", is("Course 2")));
    }

    @Test
    public void shouldReturnCourseById() throws Exception {
        long courseId = 1L;
        mockMvc.perform(get("http://localhost:8080/api/course/" + courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Course 1")));
//                .andExpect(jsonPath("$.modules").isArray())
//                .andExpect(jsonPath("$.modules", hasSize(2)))
//                .andExpect(jsonPath("$.modules[0].title", is("Module 1")))
//                .andExpect(jsonPath("$.modules[1].title", is("Module 2")));
    }

    @Test
    public void shouldReturnCourseByTitle() throws Exception {
        String title = "Course 1";
        mockMvc.perform(get("http://localhost:8080/api/course/title/" + title))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Course 1")));
    }

    @Test
    public void shouldReturnAllModulesInCourse() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/course/1/modules"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Module 1")))
                .andExpect(jsonPath("$[1].title", is("Module 2")));
    }
}