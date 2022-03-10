package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.annotation.CustomJsonRootName;
import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.mappers.CourseMapper;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.service.impl.CourseServiceImpl;
import com.epam.system_monitoring.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo swagger

@RestController
@RequestMapping("api/course")
@CrossOrigin
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    private final ResponseErrorValidation validation;

    @GetMapping("/")
    public ResponseEntity<Object> getAllCourses() {
        List<CourseDTO> courseDTOList = CourseMapper.INSTANCE.toDTOList(courseService.getAllCourses());

        Map<Object, Object> result = new HashMap<>();
        result.put(CourseDTO.class.getAnnotation(CustomJsonRootName.class).plural(), courseDTOList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCourse(@PathVariable Long id) {
        CourseDTO courseDTO = CourseMapper.INSTANCE.toDTO(courseService.getCourseById(id));

        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Object> getCourseByTitle(@PathVariable String title) {
        Course course = courseService.getCourseByTitle(title);
        CourseDTO courseDTO = CourseMapper.INSTANCE.toDTO(course);

        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<Object> getAllModulesByCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        List<ModuleDTO> dtoList = ModuleMapper.INSTANCE.toDTOList(course.getModules());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createCourse(@RequestBody @Valid CourseDTO courseDTO,
                                               BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        CourseDTO createdCourseDTO = CourseMapper.INSTANCE.toDTO(courseService.saveCourse(courseDTO));

        return new ResponseEntity<>(createdCourseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateCourse(@PathVariable Long id,
                                               @RequestBody @Valid CourseDTO courseDTO,
                                               BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        CourseDTO updatedCourseDTO = CourseMapper.INSTANCE.
                toDTO(courseService.updateCourse(id, courseDTO));

        return new ResponseEntity<>(updatedCourseDTO, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable Long id) {

        return new ResponseEntity<>(courseService.deleteCourse(id), HttpStatus.OK);
    }
}
