package com.epam.system_monitoring.impl;

import com.epam.system_monitoring.converter.ConvertModule;
import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.exception.CourseNotFoundException;
import com.epam.system_monitoring.repository.CourseRepository;
import com.epam.system_monitoring.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ConvertModule convertModule;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Course getCourseByTitle(String title) {
        return courseRepository.findByTitle(title)
                .orElseThrow(() -> new CourseNotFoundException("Course not found..."));
    }

    @Override
    public Course saveCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());

        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course oldCourse, CourseDTO newCourseDTO) {
        oldCourse.setTitle(newCourseDTO.getTitle());

        return courseRepository.save(oldCourse);
    }

    @Override
    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    //todo добавить сохранение модуля в БД или сделать добавление модуля в курс через id?
    @Override
    public Course addNewModuleToCourse(Long courseId, ModuleDTO moduleDTO) {
        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> new CourseNotFoundException("Course not found..."));
        course.getModules().add(convertModule.convertDTOToModule(moduleDTO));

        return course;
    }
}
