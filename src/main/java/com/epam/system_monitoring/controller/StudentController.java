package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Student;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.mappers.StudentMapper;
import com.epam.system_monitoring.service.impl.StudentServiceImpl;
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
@RequestMapping("api/student")
@CrossOrigin
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    private final ResponseErrorValidation validation;

    @GetMapping("/")
    public ResponseEntity<Object> getAllStudents() {
        List<StudentDTO> studentDTOList = StudentMapper.INSTANCE.toDTOList(studentService.getAllStudents());

        return new ResponseEntity<>(studentDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudent(@PathVariable Long id) {
        StudentDTO studentDTO = StudentMapper.INSTANCE.toDTO(studentService.getStudentById(id));

        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @GetMapping("/{studentId}/modules")
    public ResponseEntity<Object> getAllModulesByAssignee(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.toDTOList(student.getModules());

        return new ResponseEntity<>(moduleDTOList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createStudent(@RequestBody @Valid StudentDTO studentDTO, BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        StudentDTO createdStudentDTO = StudentMapper.INSTANCE.toDTO(studentService.saveStudent(studentDTO));

        return new ResponseEntity<>(createdStudentDTO, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
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

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        return new ResponseEntity<>(studentService.deleteStudent(id), HttpStatus.OK);
    }
}
