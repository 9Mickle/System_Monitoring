package com.epam.system_monitoring.integration.course.post;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.mappers.CourseMapper;
import com.epam.system_monitoring.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PostRequestTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    private static Course course;

    @BeforeEach
    public void setUp() {
        course = new Course(1L, "Course 1", null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldCreateNewCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO("Test title");
        Course newCourse = CourseMapper.INSTANCE.toCourse(courseDTO);
        when(courseRepository.save(newCourse)).thenReturn(newCourse);

        mockMvc.perform(post("http://localhost:8080/api/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test title")))
                .andReturn();

        verify(courseRepository).findByTitle(courseDTO.getTitle());
        verify(courseRepository).save(newCourse);
    }

    @Test
    public void shouldUpdateCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO("Updated title");
        Course updatedCourse = CourseMapper.INSTANCE.toCourse(courseDTO);
        updatedCourse.setId(course.getId());

        when(courseRepository.save(updatedCourse)).thenReturn(updatedCourse);

        mockMvc.perform(post("http://localhost:8080/api/course/update/" + course.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated title")))
                .andReturn();

        verify(courseRepository).findById(course.getId());
        verify(courseRepository).save(updatedCourse);
    }

    @Test
    public void willThrowTitleAlreadyExistExceptionWhenUpdateCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO("Course 1");
        Course testCourse = CourseMapper.INSTANCE.toCourse(courseDTO);
        course.setId(course.getId());

        when(courseRepository.findByTitle(courseDTO.getTitle())).thenReturn(Optional.of(course));
        mockMvc.perform(post("http://localhost:8080/api/course/update/" + course.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("A course with that title already exists!")))
                .andReturn();

        verify(courseRepository, never()).save(testCourse);
    }

    @Test
    public void shouldDeleteCourse() throws Exception {
        doNothing().when(courseRepository).delete(course);

        mockMvc.perform(post("http://localhost:8080/api/course/delete/" + course.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Course with id: %d was deleted", course.getId())))
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