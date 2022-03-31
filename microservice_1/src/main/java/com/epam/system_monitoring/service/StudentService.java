package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    Student updateStudent(Long id, StudentDTO studentDTO);

    Student saveStudent(StudentDTO studentDTO);

    String deleteStudent(Long id);
}
