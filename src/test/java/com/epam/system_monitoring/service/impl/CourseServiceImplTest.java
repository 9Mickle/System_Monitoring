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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private CourseServiceImpl courseService;
    @InjectMocks
    private ModuleServiceImpl moduleService;

    @BeforeEach
    public void setUp() {
        courseService = new CourseServiceImpl(courseRepository, moduleService);
        moduleService = new ModuleServiceImpl(moduleRepository);
    }

    @Test
    public void canGetAllCourses() {
        Course course = new Course();
        List<Course> exist = List.of(course);

        when(courseRepository.findAll()).thenReturn(exist);
        assertEquals(exist, courseService.getAllCourses());
        verify(courseRepository).findAll();
    }

    @Test
    public void canGetCourseById() {
        Long courseId = 1L;
        Course course = new Course();
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
        CourseDTO courseDTO = new CourseDTO();
        Course exist = CourseMapper.INSTANCE.toCourse(courseDTO);

        when(courseRepository.save(exist)).thenReturn(exist);
        assertEquals(exist, courseService.saveCourse(courseDTO));
        verify(courseRepository).save(exist);
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

//    /**
//     * Как протестировать удаление?
//     */
//    @Test
//    public void canDeleteCourse() {
//        Course course = new Course();
//        when(courseRepository.delete(course)).thenReturn();
//
//    }


    @Test
    public void canAddNewModuleInCourse() {
        Long courseId = 1L;

        CourseDTO courseDTO = new CourseDTO("Title", List.of());
        //Конвертирую ДТО, чтобы избежать NullPointerException при .getModules();
        Optional<Course> course = Optional.of(CourseMapper.INSTANCE.toCourse(courseDTO));

        ModuleDTO moduleDTO = new ModuleDTO();
        Module module = ModuleMapper.INSTANCE.toModule(moduleDTO);
        course.get().getModules().add(module);

        when(courseRepository.findById(courseId)).thenReturn(course);
//        when(moduleRepository.save(module)).thenReturn(module);??
        when(courseRepository.save(course.get())).thenReturn(course.get());
        assertEquals(course.get(), courseService.addNewModuleToCourse(courseId, moduleDTO));
    }
}