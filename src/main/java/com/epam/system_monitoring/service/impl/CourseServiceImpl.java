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
import com.epam.system_monitoring.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleServiceImpl moduleService;

    @Override
    @Transactional
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional
    public Course getCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            return course.get();
        } else {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public Course getCourseByTitle(String title) {
        Optional<Course> course = courseRepository.findByTitle(title);
        if (course.isPresent()) {
            return course.get();
        } else {
            throw new CourseNotFoundException("Course not found with title: " + title);
        }
    }

    @Override
    @Transactional
    public Course saveCourse(CourseDTO courseDTO) {
        //todo ошибку для создания такого же имени в курсе.
        return courseRepository.save(CourseMapper.INSTANCE.toCourse(courseDTO));
    }

    @Override
    @Transactional
    public Course updateCourse(String titleOldCourse, CourseDTO newCourseDTO) {
        Optional<Course> oldCourse = courseRepository.findByTitle(titleOldCourse);

        if (oldCourse.isPresent()) {
            Course course = oldCourse.get();
            course.setTitle(newCourseDTO.getTitle());
            return courseRepository.save(course);
        } else {
            throw new CourseNotFoundException("Course not found with title: " + titleOldCourse);
        }
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            courseRepository.delete(course.get());
        } else {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public Course addNewModuleToCourse(Long courseId, ModuleDTO moduleDTO) {
        Optional<Course> optCourse = courseRepository.findById(courseId);
        if (optCourse.isPresent()) {
            Course course = optCourse.get();
            Module module = moduleService.saveModule(moduleDTO);
            course.getModules().add(module);
            return course;
        } else {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
    }
}
