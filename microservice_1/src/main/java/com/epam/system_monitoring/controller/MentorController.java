package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.MentorDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Mentor;
import com.epam.system_monitoring.mappers.MentorMapper;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.mappers.StudentMapper;
import com.epam.system_monitoring.service.impl.MentorServiceImpl;
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
@RequestMapping("api/mentor")
@CrossOrigin
@RequiredArgsConstructor
public class MentorController {

    private final MentorServiceImpl mentorService;
    private final ResponseErrorValidation validation;

    @Operation(summary = "This is to fetch all the mentors from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the mentors from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = MentorDTO.class)))
            }),
    })
    @GetMapping("/")
    public ResponseEntity<Object> getAllMentors() {
        List<MentorDTO> mentorDTOList = MentorMapper.INSTANCE.toDTOList(mentorService.getAllMentors());

        return new ResponseEntity<>(mentorDTOList, HttpStatus.OK);
    }


    @Operation(summary = "Get a mentor by its id from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch mentor from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MentorDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Mentor not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getMentor(@PathVariable Long id) {
        MentorDTO mentorDTO = MentorMapper.INSTANCE.toDTO(mentorService.getMentorById(id));

        return new ResponseEntity<>(mentorDTO, HttpStatus.OK);
    }


    @Operation(summary = "Get all the modules that are assigned to the mentor from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the modules by mentor", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ModuleDTO.class)))
            }),
            @ApiResponse(responseCode = "404",
                    description = "Mentor not found", content = @Content)
    })
    @GetMapping("/{mentorId}/modules")
    public ResponseEntity<Object> getAllModulesByReporter(@PathVariable Long mentorId) {
        Mentor mentor = mentorService.getMentorById(mentorId);
        List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.toDTOList(mentor.getModules());

        return new ResponseEntity<>(moduleDTOList, HttpStatus.OK);
    }


    @Operation(summary = "Get all the students that are assigned to the mentor from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the students by mentor", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class)))
            }),
            @ApiResponse(responseCode = "404",
                    description = "Mentor not found", content = @Content)
    })
    @GetMapping("/{mentorId}/students")
    public ResponseEntity<Object> getAllStudentsByMentor(@PathVariable Long mentorId) {
        Mentor mentor = mentorService.getMentorById(mentorId);
        List<StudentDTO> studentDTOList = StudentMapper.INSTANCE.toDTOList(mentor.getStudents());

        return new ResponseEntity<>(studentDTOList, HttpStatus.OK);
    }


    @Operation(summary = "Create a new mentor and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "A Mentor has been created", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MentorDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Сan not create mentors with the same username or email",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Object> createMentor(@RequestBody @Valid MentorDTO mentorDTO, BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        MentorDTO createdMentorDTO = MentorMapper.INSTANCE.toDTO(mentorService.saveMentor(mentorDTO));

        return new ResponseEntity<>(createdMentorDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Update mentor and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A mentor has been updated", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MentorDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Mentor not found", content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "A mentor with that username or email already exists. " +
                            "It is also not possible to update the mentor's username", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMentor(@PathVariable Long id,
                                               @RequestBody @Valid MentorDTO mentorDTO,
                                               BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        MentorDTO updatedMentorDTO = MentorMapper.INSTANCE.toDTO(mentorService.updateMentor(id, mentorDTO));

        return new ResponseEntity<>(updatedMentorDTO, HttpStatus.OK);
    }


    @Operation(summary = "Delete mentor from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A mentor has been deleted", content = {
                    @Content(
                            mediaType = MediaType.TEXT_HTML_VALUE,
                            examples = @ExampleObject("Mentor with id was deleted"))}),
            @ApiResponse(responseCode = "404",
                    description = "Mentor not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMentor(@PathVariable Long id) {
        return new ResponseEntity<>(mentorService.deleteMentor(id), HttpStatus.OK);
    }


    @Operation(summary = "Assign a new student to the mentor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The student was assigned to a mentor", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MentorDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Mentor or student not found", content = @Content)
    })
    @PostMapping("/{mentorId}/{studentId}")
    public ResponseEntity<Object> addStudentToMentor(@PathVariable Long mentorId, @PathVariable Long studentId) {
        MentorDTO mentorDTO = MentorMapper.INSTANCE.toDTO(mentorService.addStudent(mentorId, studentId));

        return new ResponseEntity<>(mentorDTO, HttpStatus.OK);
    }
}
