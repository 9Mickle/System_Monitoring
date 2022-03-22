package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Student;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.mappers.StudentMapper;
import com.epam.system_monitoring.service.impl.StudentServiceImpl;
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
@RequestMapping("api/student")
@CrossOrigin
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;
    private final ResponseErrorValidation validation;

    @Operation(summary = "This is to fetch all the students from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the students from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class)))
            }),
    })
    @GetMapping("/")
    public ResponseEntity<Object> getAllStudents() {
        List<StudentDTO> studentDTOList = StudentMapper.INSTANCE.toDTOList(studentService.getAllStudents());

        return new ResponseEntity<>(studentDTOList, HttpStatus.OK);
    }


    @Operation(summary = "Get a student by its id from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch student from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Student not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudent(@PathVariable Long id) {
        StudentDTO studentDTO = StudentMapper.INSTANCE.toDTO(studentService.getStudentById(id));

        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }


    @Operation(summary = "Get all the modules that are assigned to the student from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the modules by student", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ModuleDTO.class)))
            }),
            @ApiResponse(responseCode = "404",
                    description = "Student not found", content = @Content)
    })
    @GetMapping("/{studentId}/modules")
    public ResponseEntity<Object> getAllModulesByAssignee(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.toDTOList(student.getModules());

        return new ResponseEntity<>(moduleDTOList, HttpStatus.OK);
    }


    @Operation(summary = "Create a new student and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "A student has been created", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Сan not create students with the same username",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createStudent(@RequestBody @Valid StudentDTO studentDTO, BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        StudentDTO createdStudentDTO = StudentMapper.INSTANCE.toDTO(studentService.saveStudent(studentDTO));

        return new ResponseEntity<>(createdStudentDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Update student and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A student has been updated", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Student not found", content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "A student with that username already exists. " +
                            "It is also not possible to update the student's username", content = @Content)
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long id,
                                                @RequestBody @Valid StudentDTO studentDTO,
                                                BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        StudentDTO updatedStudentDTO = StudentMapper.INSTANCE.toDTO(studentService.updateStudent(id, studentDTO));

        return new ResponseEntity<>(updatedStudentDTO, HttpStatus.OK);
    }


    @Operation(summary = "Delete student from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A student has been deleted", content = {
                    @Content(
                            mediaType = MediaType.TEXT_HTML_VALUE,
                            examples = @ExampleObject("Student with id was deleted"))}),
            @ApiResponse(responseCode = "404",
                    description = "Student not found", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        return new ResponseEntity<>(studentService.deleteStudent(id), HttpStatus.OK);
    }
}
