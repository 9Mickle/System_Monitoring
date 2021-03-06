package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.exception.found.CourseNotFoundException;
import com.epam.system_monitoring.exception.exist.TitleAlreadyExistException;
import com.epam.system_monitoring.mappers.CourseMapper;
import com.epam.system_monitoring.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    private static Course course;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    public void setUp() {
        course = new Course(1L, "Course 1", null);
    }

    @Test
    public void canGetAllCourses() {
        List<Course> courses = List.of(
                course,
                new Course(2L, "Course 2", null));

        when(courseRepository.findAll()).thenReturn(courses);
        assertEquals(courses, courseService.getAllCourses());
        verify(courseRepository).findAll();
    }

    @Test
    public void canGetCourseById() {
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        assertEquals(course, courseService.getCourseById(courseId));
        verify(courseRepository).findById(courseId);
    }

    @Test
    public void canGetCourseByTitle() {
        String title = "Course 1";

        when(courseRepository.findByTitle(title)).thenReturn(Optional.of(course));
        assertEquals(course, courseService.getCourseByTitle(title));
        verify(courseRepository).findByTitle(title);
    }

    @Test
    public void willThrowCourseNotFoundExceptionById() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenThrow(CourseNotFoundException.class);
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(courseId));
    }

    @Test
    public void willThrowCourseNotFoundExceptionByTitle() {
        String title = "Course 1";
        when(courseRepository.findByTitle(title)).thenThrow(CourseNotFoundException.class);
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseByTitle(title));
    }

    @Test
    public void canSaveCourse() {
        course.setId(null);
        CourseDTO courseDTO = CourseMapper.INSTANCE.toDTO(course);

        when(courseRepository.save(course)).thenReturn(course);
        assertEquals(course, courseService.saveCourse(courseDTO));
        verify(courseRepository).save(course);
    }

    @Test
    public void willThrowTitleAlreadyExistException() {
        String sameTitle = "Course 1";
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle(sameTitle);

        when(courseRepository.findByTitle(sameTitle)).thenReturn(Optional.of(course));
        assertThrows(TitleAlreadyExistException.class, () -> courseService.updateCourse(1L, courseDTO));
        verify(courseRepository).findByTitle(sameTitle);
    }

    @Test
    public void canUpdateCourse() {
        Course updatedCourse = new Course();
        String newTitle = "Course 2";

        updatedCourse.setId(null);
        updatedCourse.setTitle(newTitle);
        CourseDTO updatedCourseDTO = CourseMapper.INSTANCE.toDTO(updatedCourse);

        when(courseRepository.save(updatedCourse)).thenReturn(updatedCourse);
        assertEquals(newTitle, courseService.saveCourse(updatedCourseDTO).getTitle());
        verify(courseRepository).save(updatedCourse);
    }

    @Test
    public void canDeleteCourse() {
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).deleteById(course.getId());
        courseService.deleteCourse(course.getId());
        verify(courseRepository, times(1)).deleteById(course.getId());
    }
}