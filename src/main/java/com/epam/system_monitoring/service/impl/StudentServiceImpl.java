package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Student;
import com.epam.system_monitoring.exception.StudentNotFoundException;
import com.epam.system_monitoring.exception.UsernameAlreadyExistException;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.mappers.StudentMapper;
import com.epam.system_monitoring.repository.StudentRepository;
import com.epam.system_monitoring.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    /**
     * Получить стуеднта по id.
     *
     * @param id студента.
     * @return студент.
     */
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }

    /**
     * Получить модули, который закреплены за студентом.
     *
     * @param studentId id студента.
     * @return список модулей.
     */
    @Override
    public List<ModuleDTO> getAllModulesByAssignee(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        return student.getModules()
                .stream()
                .map(ModuleMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Обновить студента.
     *
     * @param id         студента.
     * @param studentDTO студент переданный с клиента.
     * @return обновленный студент.
     */
    @Override
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        if (studentRepository.existsByUsername(studentDTO.getUsername())) {
            throw new UsernameAlreadyExistException("A student with that username already exists");
        }

        student.setName(studentDTO.getName());
        student.setSurname(studentDTO.getSurname());
        student.setUsername(studentDTO.getUsername());
        return studentRepository.save(student);

    }

    /**
     * Сохранить нового студента.
     *
     * @param studentDTO студент переданный с клиента.
     * @return новый студент.
     */
    @Override
    public Student saveStudent(StudentDTO studentDTO) {
        Optional<Student> optStudent = studentRepository.findByUsername(studentDTO.getUsername());

        if (optStudent.isEmpty()) {
            Student student = StudentMapper.INSTANCE.toStudent(studentDTO);
            return studentRepository.save(student);
        } else {
            throw new UsernameAlreadyExistException("A username already exists");
        }
    }

    /**
     * Удалить студента.
     *
     * @param id студента.
     * @return студент.
     */
    @Override
    public String deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        studentRepository.delete(student);
        return String.format("Student with id: %d was deleted", id);
    }
}
