package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.MentorDTO;
import com.epam.system_monitoring.entity.Mentor;
import com.epam.system_monitoring.entity.Student;
import com.epam.system_monitoring.exception.MentorNotFoundException;
import com.epam.system_monitoring.exception.ModuleNotFoundException;
import com.epam.system_monitoring.exception.UsernameAlreadyExistException;
import com.epam.system_monitoring.mappers.MentorMapper;
import com.epam.system_monitoring.repository.MentorRepository;
import com.epam.system_monitoring.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    private final StudentServiceImpl studentService;

    @Override
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    /**
     * Получить ментора по id.
     *
     * @param id ментора.
     * @return ментор.
     */
    @Override
    public Mentor getMentorById(Long id) {
        return mentorRepository.findById(id)
                .orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + id));
    }

    //обновить можно, только если изменится username.
    /**
     * Обновить ментора.
     *
     * @param id        ментора.
     * @param mentorDTO полученный с клиента ментор.
     * @return обновленный ментор.
     */
    @Override
    public Mentor updateMentor(Long id, MentorDTO mentorDTO) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + id));

        if (!mentor.getUsername().equals(mentorDTO.getUsername())) {
            mentor.setName(mentorDTO.getName());
            mentor.setSurname(mentorDTO.getSurname());
            mentor.setUsername(mentorDTO.getUsername());
            return mentorRepository.save(mentor);
        } else {
            throw new UsernameAlreadyExistException("A mentor with that username already exists");
        }
    }

    /**
     * Сохранить нового ментора.
     *
     * @param mentorDTO ментор полученный с клиента.
     * @return новый ментор.
     */
    @Override
    public Mentor saveMentor(MentorDTO mentorDTO) {
        mentorRepository.findByUsername(mentorDTO.getUsername())
                .ifPresent(m -> {
                    throw new UsernameAlreadyExistException("A username already exists");
                });

        Mentor mentor = MentorMapper.INSTANCE.toMentor(mentorDTO);
        return mentorRepository.save(mentor);
    }

    /**
     * Добавление ментору существующего стуента.
     *
     * @param mentorId  id ментора.
     * @param studentId id студента.
     * @return ментор.
     */
    @Override
    @Transactional
    public Mentor addStudent(Long mentorId, Long studentId) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ModuleNotFoundException("Mentor not found with id: " + mentorId));

        Student student = studentService.getStudentById(studentId);
        student.setMentor(mentor);
        mentor.getStudents().add(student);
        return mentorRepository.save(mentor);
    }

    /**
     * Удалить ментора.
     *
     * @param id ментора.
     * @return строка.
     */
    @Override
    public String deleteMentor(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + id));

        mentorRepository.delete(mentor);
        return String.format("Mentor with id: %d was deleted", id);
    }
}
