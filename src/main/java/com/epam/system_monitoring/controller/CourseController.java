package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.converter.ConvertCourse;
import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.impl.CourseServiceImpl;
import com.epam.system_monitoring.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/course")
@CrossOrigin
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    private final ConvertCourse convertCourse;

    private final ResponseErrorValidation validation;

    @GetMapping("/")
    public ResponseEntity<Object> getAllCourses() {
        List<CourseDTO> courseDTOList = courseService.getAllCourses()
                .stream()
                .map(convertCourse::courseToCourseDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(courseDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCourse(@PathVariable Long id) {
        try {
            Optional<Course> course = courseService.getCourseById(id);
            if (course.isPresent()) {
                CourseDTO courseDTO = convertCourse.courseToCourseDTO(course.get());

                return new ResponseEntity<>(courseDTO, HttpStatus.OK);
            } else {
                return courseNotFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<Object> getCourseByTitle(@PathVariable String title) {
        Course course = courseService.getCourseByTitle(title);
        CourseDTO courseDTO = convertCourse.courseToCourseDTO(course);

        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<Object> getAllModulesInCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId).get();

        return new ResponseEntity<>(course.getModules(), HttpStatus.OK);
    }


    /**
     * Первый вид обработки исключений клиента.
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createCourse(@RequestBody @Valid CourseDTO courseDTO,
                                               BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        return new ResponseEntity<>(courseService.saveCourse(courseDTO), HttpStatus.CREATED);
    }

    /**
     * Второй вид обработки исключений клиента.
     */
    @PostMapping("/{courseId}/modules/")
    public ResponseEntity<Object> createModuleForCourse(@PathVariable Long courseId,
                                                        @RequestBody @Valid ModuleDTO moduleDTO) {
        try {
            return new ResponseEntity<>(courseService.addNewModuleToCourse(courseId, moduleDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateCourse(@PathVariable Long id,
                                               @RequestBody @Valid CourseDTO courseDTO) {
        Course oldCourse = courseService.getCourseById(id).get();

        return new ResponseEntity<>(courseService.updateCourse(oldCourse, courseDTO), HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id).get();
        courseService.deleteCourse(course);

        return new ResponseEntity<>(String.format("Course with id: %d was deleted", id), HttpStatus.OK);
    }

    private ResponseEntity<Object> errorResponse() {
        return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> courseNotFoundResponse(Long id) {
        return new ResponseEntity<>(String.format("Course not found with id: %d", id), HttpStatus.NOT_FOUND);
    }
}
