package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getAllCourses();

    Optional<Course> getCourseById(Long courseId);

    Course getCourseByTitle(String title);

    Course saveCourse(CourseDTO courseDTO);

    Course updateCourse(Course oldCourse, CourseDTO newCourseDTO);

    void deleteCourse(Course course);

    Course addNewModuleToCourse(Long courseId, ModuleDTO moduleDTO);
}
