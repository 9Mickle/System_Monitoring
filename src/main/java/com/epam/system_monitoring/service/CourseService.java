package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(Long courseId);

    Course getCourseByTitle(String title);

    Course saveCourse(CourseDTO courseDTO);

    Course updateCourse(Long id, CourseDTO newCourseDTO);

    String deleteCourse(Long id);

//    Course createNewModuleToCourse(Long courseId, ModuleDTO moduleDTO);
}
