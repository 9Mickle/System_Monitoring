package com.epam.system_monitoring.repository;

import com.epam.system_monitoring.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;

    @BeforeEach
    public void setUp() {
        course = new Course(1L, "Course 1", List.of());
    }

    @Test
    public void test() {

    }
    @Test
    public void testSave() {
        Course savedCourse = courseRepository.save(course);
        assertEquals(course, savedCourse);
    }

}