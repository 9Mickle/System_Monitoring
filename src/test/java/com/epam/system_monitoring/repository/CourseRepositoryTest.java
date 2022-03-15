package com.epam.system_monitoring.repository;

import com.epam.system_monitoring.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseRepositoryTest {

    @Mock
    private CourseRepository courseRepository;

    private static Course course;
    @BeforeEach
    public void setUp() {
        course = new Course(1L, "Course 1", List.of());
    }

    @Test
    void findByIdTest() {
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        assertEquals(Optional.of(course), courseRepository.findById(course.getId()));
        verify(courseRepository).findById(course.getId());
    }

    @Test
    void findByTitleTest() {
        when(courseRepository.findByTitle(course.getTitle())).thenReturn(Optional.of(course));
        assertEquals(Optional.of(course), courseRepository.findByTitle(course.getTitle()));
        verify(courseRepository).findByTitle(course.getTitle());
    }

    @Test
    void existsByTitleTest() {
        when(courseRepository.existsByTitle(course.getTitle())).thenReturn(true);
        assertEquals(true, courseRepository.existsByTitle(course.getTitle()));
        verify(courseRepository).existsByTitle(course.getTitle());
    }

    @Test
    void nonExistsByTitleTest() {
        when(courseRepository.existsByTitle("Some title")).thenReturn(false);
        assertEquals(false, courseRepository.existsByTitle("Some title"));
        verify(courseRepository).existsByTitle("Some title");
    }

    @Test
    void deleteByIdTest() {
        doNothing().when(courseRepository).deleteById(course.getId());
        courseRepository.deleteById(course.getId());
        verify(courseRepository).deleteById(course.getId());
    }
}