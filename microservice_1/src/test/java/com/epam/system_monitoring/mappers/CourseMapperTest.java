package com.epam.system_monitoring.mappers;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.entity.Course;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseMapperTest {

    @Test
    void toDTOTest() {
        Course course = new Course(1L, "Course 1", List.of());
        CourseDTO courseDTO = CourseMapper.INSTANCE.toDTO(course);

        assertEquals(course.getTitle(), courseDTO.getTitle());
    }

    @Test
    void toCourseTest() {
        CourseDTO courseDTO = new CourseDTO("Course 1");
        Course course = CourseMapper.INSTANCE.toCourse(courseDTO);

        assertEquals(courseDTO.getTitle(), course.getTitle());
    }

    @Test
    void toDTOListTest() {
        List<Course> courses = List.of(
                new Course(1L, "Course 1", List.of()),
                new Course(2L, "Course 2", List.of()));

        List<CourseDTO> courseDTOList = CourseMapper.INSTANCE.toDTOList(courses);

        assertEquals(courses.size(), courseDTOList.size());
        assertEquals(courses.get(0).getTitle(), courseDTOList.get(0).getTitle());
        assertEquals(courses.get(1).getTitle(), courseDTOList.get(1).getTitle());
    }
}