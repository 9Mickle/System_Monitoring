package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.mappers.CourseMapper;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.service.impl.CourseServiceImpl;
import com.epam.system_monitoring.validation.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/course")
@CrossOrigin
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;
    private final ResponseErrorValidation validation;

    @Operation(summary = "This is to fetch all the courses from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the courses from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CourseDTO.class)))
            }),
    })
    @GetMapping("/")
    public ResponseEntity<Object> getAllCourses() {
        List<CourseDTO> courseDTOList = CourseMapper.INSTANCE.toDTOList(courseService.getAllCourses());

        return new ResponseEntity<>(courseDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Get a course by its id from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch course from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Course not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCourse(@PathVariable Long id) {
        CourseDTO courseDTO = CourseMapper.INSTANCE.toDTO(courseService.getCourseById(id));

        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get a course by its title from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch course from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Course not found", content = @Content)
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<Object> getCourseByTitle(@PathVariable String title) {
        Course course = courseService.getCourseByTitle(title);
        CourseDTO courseDTO = CourseMapper.INSTANCE.toDTO(course);

        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all the modules that were created in the course from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the modules by course", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ModuleDTO.class)))
            }),
            @ApiResponse(responseCode = "404",
                    description = "Course not found", content = @Content)
    })
    @GetMapping("/{courseId}/modules")
    public ResponseEntity<Object> getAllModulesByCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        List<ModuleDTO> dtoList = ModuleMapper.INSTANCE.toDTOList(course.getModules());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }


    @Operation(summary = "Create a new course and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "A Course has been created", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseDTO.class))})
    })
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

    @Operation(summary = "Update course and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A Course has been updated", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Course not found", content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "A course with that title already exists", content = {
                    @Content(
                            mediaType = MediaType.TEXT_HTML_VALUE,
                            examples = @ExampleObject("A course with that title already exists!")
                    )
            })
    })
    @PutMapping("/update/{id}")
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

    @Operation(summary = "Delete course from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A Course has been deleted", content = {
                    @Content(
                            mediaType = MediaType.TEXT_HTML_VALUE,
                            examples = @ExampleObject("Course with id was deleted"))}),
            @ApiResponse(responseCode = "404",
                    description = "Course not found", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable Long id) {

        return new ResponseEntity<>(courseService.deleteCourse(id), HttpStatus.OK);
    }
}
