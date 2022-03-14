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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/")
    public ResponseEntity<Object> getAllMentors() {
        List<MentorDTO> mentorDTOList = MentorMapper.INSTANCE.toDTOList(mentorService.getAllMentors());

        return new ResponseEntity<>(mentorDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMentor(@PathVariable Long id) {
        MentorDTO mentorDTO = MentorMapper.INSTANCE.toDTO(mentorService.getMentorById(id));

        return new ResponseEntity<>(mentorDTO, HttpStatus.OK);
    }

    @GetMapping("/{mentorId}/modules")
    public ResponseEntity<Object> getAllModulesByReporter(@PathVariable Long mentorId) {
        Mentor mentor = mentorService.getMentorById(mentorId);
        List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.toDTOList(mentor.getModules());

        return new ResponseEntity<>(moduleDTOList, HttpStatus.OK);
    }

    @GetMapping("/{mentorId}/students")
    public ResponseEntity<Object> getAllStudentsByMentor(@PathVariable Long mentorId) {
        Mentor mentor = mentorService.getMentorById(mentorId);
        List<StudentDTO> studentDTOList = StudentMapper.INSTANCE.toDTOList(mentor.getStudents());

        return new ResponseEntity<>(studentDTOList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createMentor(@RequestBody @Valid MentorDTO mentorDTO, BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        MentorDTO createdMentorDTO = MentorMapper.INSTANCE.toDTO(mentorService.saveMentor(mentorDTO));

        return new ResponseEntity<>(createdMentorDTO, HttpStatus.CREATED);
    }

    @PostMapping("/update/{id}")
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

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMentor(@PathVariable Long id) {
        return new ResponseEntity<>(mentorService.deleteMentor(id), HttpStatus.OK);
    }

    @PostMapping("/add/{mentorId}/{studentId}")
    public ResponseEntity<Object> addStudentToMentor(@PathVariable Long mentorId, @PathVariable Long studentId) {
        MentorDTO mentorDTO = MentorMapper.INSTANCE.toDTO(mentorService.addStudent(mentorId, studentId));

        return new ResponseEntity<>(mentorDTO, HttpStatus.OK);
    }
}
