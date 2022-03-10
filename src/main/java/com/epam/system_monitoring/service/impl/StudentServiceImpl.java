package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Student;
import com.epam.system_monitoring.exception.StudentNotFoundException;
import com.epam.system_monitoring.exception.UsernameAlreadyExistException;
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

    //обновить можно, только если изменится username, а так не должно быть...
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

        if (!student.getUsername().equals(studentDTO.getUsername())) {
            student.setName(studentDTO.getName());
            student.setSurname(studentDTO.getSurname());
            student.setUsername(studentDTO.getUsername());
            return studentRepository.save(student);
        } else {
            throw new UsernameAlreadyExistException("A student with that username already exists");
        }

    }

    /**
     * Сохранить нового студента.
     *
     * @param studentDTO студент переданный с клиента.
     * @return новый студент.
     */
    @Override
    public Student saveStudent(StudentDTO studentDTO) {
        studentRepository.findByUsername(studentDTO.getUsername())
                .ifPresent(s -> {
                    throw new UsernameAlreadyExistException("A username already exists");
                });

        Student student = StudentMapper.INSTANCE.toStudent(studentDTO);
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
