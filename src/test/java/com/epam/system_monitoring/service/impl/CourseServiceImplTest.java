package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.exception.CourseNotFoundException;
import com.epam.system_monitoring.mappers.CourseMapper;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.repository.CourseRepository;
import com.epam.system_monitoring.repository.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModuleRepository moduleRepository;

    private CourseServiceImpl courseService;
    private ModuleServiceImpl moduleService;

    @BeforeEach
    public void setUp() {
        courseService = new CourseServiceImpl(courseRepository, moduleService);
        moduleService = new ModuleServiceImpl(moduleRepository);
    }

    @Test
    public void canGetAllCourses() {
        CourseDTO courseDTO1 = new CourseDTO("Title1", List.of());
        CourseDTO courseDTO2 = new CourseDTO("Title2", List.of());

        List<Course> exist = List.of(CourseMapper.INSTANCE.toCourse(courseDTO1),
                CourseMapper.INSTANCE.toCourse(courseDTO2));

        when(courseRepository.findAll()).thenReturn(exist);

        assertEquals(exist, courseService.getAllCourses());

        verify(courseRepository).findAll();
    }

    @Test
    public void canGetCourseById() {
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO("Title", List.of());
        Course course = CourseMapper.INSTANCE.toCourse(courseDTO);
        Optional<Course> exist = Optional.of(course);

        when(courseRepository.findById(courseId)).thenReturn(exist);

        assertEquals(exist.get(), courseService.getCourseById(courseId));

        verify(courseRepository).findById(courseId);
    }

    @Test
    public void canGetCourseByTitle() {
        String title = "Course 1";
        CourseDTO courseDTO = new CourseDTO(title, List.of());
        Course course = CourseMapper.INSTANCE.toCourse(courseDTO);
        Optional<Course> exist = Optional.of(course);

        when(courseRepository.findByTitle(title)).thenReturn(exist);

        assertEquals(exist.get(), courseService.getCourseByTitle(title));

        verify(courseRepository).findByTitle(title);
    }

    @Test
    public void willThrowCourseNotFoundExceptionWithId() {
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(2L));
    }

    @Test
    public void willThrowCourseNotFoundExceptionWithTitle() {
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseByTitle("Test title"));
    }

    /**
     * Правильно?
     */
    @Test
    public void canSaveCourse() {
        CourseDTO courseDTO = new CourseDTO("Course 1", List.of());
        Course exist = CourseMapper.INSTANCE.toCourse(courseDTO);
        courseService.saveCourse(courseDTO);

        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        verify(courseRepository).save(courseArgumentCaptor.capture());

        Course capturedCourse = courseArgumentCaptor.getValue();

        assertEquals(exist, capturedCourse);
    }

    /**
     * Как не дублировать тесты?
     */
    @Test
    public void canUpdateCourse() {
        String oldCourseTitle = "Course 1";

        CourseDTO courseDTO = new CourseDTO(oldCourseTitle, List.of());
        Optional<Course> existOldCourse = Optional.of(CourseMapper.INSTANCE.toCourse(courseDTO));

        CourseDTO updatedCourseDTO = new CourseDTO("Course 2", List.of());
        Course updatedCourse = CourseMapper.INSTANCE.toCourse(updatedCourseDTO);

        when(courseRepository.findByTitle(oldCourseTitle)).thenReturn(existOldCourse);
        assertEquals(existOldCourse.get(), courseService.getCourseByTitle(oldCourseTitle));
        verify(courseRepository).findByTitle(oldCourseTitle);

        when(courseRepository.save(updatedCourse)).thenReturn(updatedCourse);
        assertEquals(updatedCourse, courseService.updateCourse(oldCourseTitle, updatedCourseDTO));
        verify(courseRepository).save(updatedCourse);
    }

    /**
     * Как протестировать удаление?
     */
//    @Test
//    public void canDeleteCourse() {
//        when(courseRepository.findAll().size()).thenReturn(0);
//
//        Optional<Course> course = Optional.of(CourseMapper.INSTANCE.toCourse(courseDTO1));
//
//        when(courseRepository.delete()).thenReturn(listCourses);
//    }

    /**
     * Получаю NullPointer на 93 строке в CourseServiceImpl.
     */
    @Test
    public void canAddNewModuleInCourse() {
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO("Title", List.of());
        Optional<Course> course = Optional.of(CourseMapper.INSTANCE.toCourse(courseDTO));

        ModuleDTO moduleDTO = new ModuleDTO();
        Module module = ModuleMapper.INSTANCE.toModule(moduleDTO);
        course.get().getModules().add(module);

        given(courseRepository.findById(courseId)).willReturn(course);
        given(moduleRepository.save(module)).willReturn(module);
        when(courseRepository.save(course.get())).thenReturn(course.get());
        assertEquals(course.get(), courseService.addNewModuleToCourse(courseId, moduleDTO));
    }
}