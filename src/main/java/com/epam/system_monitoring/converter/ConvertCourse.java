package com.epam.system_monitoring.converter;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class ConvertCourse {

    public CourseDTO courseToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle(course.getTitle());
        return courseDTO;
    }
}
