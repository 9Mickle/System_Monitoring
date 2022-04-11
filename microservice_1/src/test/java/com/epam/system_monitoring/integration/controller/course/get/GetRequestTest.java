package com.epam.system_monitoring.integration.controller.course.get;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.MentorDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.ModuleStatus;
import com.epam.system_monitoring.service.impl.CourseServiceImpl;
import com.epam.system_monitoring.service.impl.MentorServiceImpl;
import com.epam.system_monitoring.service.impl.ModuleServiceImpl;
import com.epam.system_monitoring.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Перед тестом #docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management
 */
//@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
// дроп и создание новой бд после каждого теста.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@ContextConfiguration
class GetRequestTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private ModuleServiceImpl moduleService;
    @Autowired
    private MentorServiceImpl mentorService;
    @Autowired
    private StudentServiceImpl studentService;

    private static CourseDTO courseDTO;
    private static ModuleDTO moduleDTO;
    private static MentorDTO mentorDTO;
    private static StudentDTO studentDTO;

//    @Container
//    public final static RabbitMQContainer rabbitMQContainer =
//            new RabbitMQContainer("rabbitmq:3.9-management").withReuse(false);
//
//    @DynamicPropertySource
//    static void configure(DynamicPropertyRegistry registry) {
//        registry.add("spring.rabbitmq.host", rabbitMQContainer::getContainerIpAddress);
//        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
//    }

    @BeforeEach
    public void setUp() {
//        rabbitMQContainer.start();
        LocalDateTime startDate = LocalDateTime.now();
        mentorDTO = new MentorDTO("mentor1", "mentor1", "mentorik1", "email", "123");
        studentDTO = new StudentDTO("student1", "stud1", "studentik1");
        courseDTO = new CourseDTO("Course 1");
        moduleDTO = new ModuleDTO(
                1L,
                "Module 1",
                "Lorem",
                null,
                startDate.plus(1, ChronoUnit.DAYS),
                ModuleStatus.TODO,
                1L,
                1L);

        mentorService.saveMentor(mentorDTO);
        studentService.saveStudent(studentDTO);
        courseService.saveCourse(courseDTO);
        moduleService.saveModule(moduleDTO);
    }

    @Test
    public void shouldReturnAllCourses() throws Exception {
        CourseDTO courseDTO = new CourseDTO("Course 2");
        courseService.saveCourse(courseDTO);

        mockMvc.perform(get("http://localhost:8080/api/course/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Course 1")))
                .andExpect(jsonPath("$[1].title", is("Course 2")));
    }

    @Test
    public void shouldReturnCourseById() throws Exception {
        long courseId = 1L;
        mockMvc.perform(get("http://localhost:8080/api/course/" + courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Course 1")));
    }

    @Test
    public void shouldReturnNotFoundExceptionById() throws Exception {
        long non_existing_id = 2L;
        mockMvc.perform(get("http://localhost:8080/api/course/" + non_existing_id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Course not found with id: " + non_existing_id)));
    }

    @Test
    public void shouldReturnNotFoundExceptionByTitle() throws Exception {
        String non_existing_title = "Course 2";
        mockMvc.perform(get("http://localhost:8080/api/course/title/" + non_existing_title))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Course not found with title: " + non_existing_title)));
    }

    @Test
    public void shouldReturnCourseByTitle() throws Exception {
        String title = "Course 1";
        mockMvc.perform(get("http://localhost:8080/api/course/title/" + title))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(title)));
    }

    @Test
    public void shouldReturnAllModulesInCourse() throws Exception {
        int courseId = Math.toIntExact(moduleDTO.getCourseId());
        int assigneeId = Math.toIntExact(moduleDTO.getAssigneeId());
        int reporterId = Math.toIntExact(moduleDTO.getReporterId());

        String deadline = moduleDTO.getDeadline().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String moduleStatus = String.valueOf(moduleDTO.getModuleStatus());
        String url = String.format("http://localhost:8080/api/course/%d/modules", courseId);

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(moduleDTO.getTitle())))
                .andExpect(jsonPath("$[0].description", is(moduleDTO.getDescription())))
                .andExpect(jsonPath("$[0].courseId", is(courseId)))
                .andExpect(jsonPath("$[0].deadline", is(deadline)))
                .andExpect(jsonPath("$[0].moduleStatus", is(moduleStatus)))
                .andExpect(jsonPath("$[0].assigneeId", is(assigneeId)))
                .andExpect(jsonPath("$[0].reporterId", is(reporterId)));
    }
}