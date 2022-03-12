package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Student;
import com.epam.system_monitoring.exception.data.DataCannotBeChangedException;
import com.epam.system_monitoring.exception.data.NotEnoughDataException;
import com.epam.system_monitoring.exception.found.StudentNotFoundException;
import com.epam.system_monitoring.exception.exist.UsernameAlreadyExistException;
import com.epam.system_monitoring.mappers.StudentMapper;
import com.epam.system_monitoring.repository.StudentRepository;
import com.epam.system_monitoring.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

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
     * Сохранить нового студента.
     *
     * @param studentDTO студент переданный с клиента.
     * @return новый студент.
     */
    @Override
    public Student saveStudent(StudentDTO studentDTO) {
        if (studentDTO.getUsername() == null) {
            throw new NotEnoughDataException("Username is missing for saving");
        }

        studentRepository.findByUsername(studentDTO.getUsername())
                .ifPresent(s -> {
                    throw new UsernameAlreadyExistException("A username already exists");
                });

        Student student = StudentMapper.INSTANCE.toStudent(studentDTO);
        return studentRepository.save(student);
    }

    /**
     * Обновить студента. Обновить поле username нельзя.
     *
     * @param id         студента.
     * @param studentDTO студент переданный с клиента.
     * @return обновленный студент.
     */
    @Override
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        if (studentDTO.getUsername() != null) {
            throw new DataCannotBeChangedException("Username cannot be updated!");
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        student.setName(studentDTO.getName());
        student.setSurname(studentDTO.getSurname());
        return studentRepository.save(student);
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
