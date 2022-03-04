package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Mentor;
import com.epam.system_monitoring.entity.Student;

import java.util.List;

public interface StudentService {

    Student getStudentById(Long id);

    Student updateStudent(Long id, StudentDTO studentDTO);

    Student saveStudent(StudentDTO studentDTO);

    List<ModuleDTO> getAllModulesByAssignee(Long studentId);

    String deleteStudent(Long id);
}
