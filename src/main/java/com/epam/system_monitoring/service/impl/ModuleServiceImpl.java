package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.entity.Mentor;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.entity.Student;
import com.epam.system_monitoring.exception.found.CourseNotFoundException;
import com.epam.system_monitoring.exception.found.MentorNotFoundException;
import com.epam.system_monitoring.exception.found.ModuleNotFoundException;
import com.epam.system_monitoring.exception.found.StudentNotFoundException;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.repository.CourseRepository;
import com.epam.system_monitoring.repository.MentorRepository;
import com.epam.system_monitoring.repository.ModuleRepository;
import com.epam.system_monitoring.repository.StudentRepository;
import com.epam.system_monitoring.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<Module> getAllModule() {
        return moduleRepository.findAll();
    }

    /**
     * Получить модуль по id.
     *
     * @param id модуля.
     * @return модуль.
     */
    @Override
    public Module getModuleById(Long id) {
        Optional<Module> module = moduleRepository.findById(id);

        if (module.isPresent()) {
            return module.get();
        } else {
            throw new ModuleNotFoundException("Module not found with id: " + id);
        }
    }

    /**
     * Получить модуль по названию.
     *
     * @param title название модуля.
     * @return модуль.
     */
    @Override
    public Module getModuleByTitle(String title) {
        Optional<Module> optModule = moduleRepository.findByTitle(title);

        if (optModule.isPresent()) {
            return optModule.get();
        } else {
            throw new ModuleNotFoundException("Module not found with title: " + title);
        }
    }

    @Override
    @Transactional
    public Module saveModule(ModuleDTO moduleDTO) {

        Course course = courseRepository.findById((moduleDTO.getCourseId()))
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + moduleDTO.getCourseId()));

        Mentor mentor = mentorRepository.findById((moduleDTO.getReporterId()))
                .orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + moduleDTO.getReporterId()));

        Module module = ModuleMapper.INSTANCE.toModule(moduleDTO);

        module.setCourse(course);
        module.setReporter(mentor);
        module.setStartDate(LocalDateTime.now());

        course.getModules().add(module);
        mentor.getModules().add(module);

        if (moduleDTO.getAssigneeId() != null) {
            Student student = studentRepository.findById(moduleDTO.getAssigneeId())
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + moduleDTO.getAssigneeId()));

            module.setAssignee(student);
            student.getModules().add(module);
        }

        return moduleRepository.save(module);
    }

    /**
     * Обновить модуль.
     *
     * @param id        модуля.
     * @param moduleDTO модуль переданный с клиента.
     * @return модуль.
     */
    @Override
    @Transactional
    public Module updateModule(Long id, ModuleDTO moduleDTO) {

        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found with id: " + id));

        Student studentById = studentRepository.findById(moduleDTO.getAssigneeId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + moduleDTO.getAssigneeId()));

        Mentor mentorById = mentorRepository.findById((moduleDTO.getReporterId()))
                .orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + moduleDTO.getReporterId()));

        module.setTitle(moduleDTO.getTitle());
        module.setDescription(moduleDTO.getDescription());
        module.setDeadline(moduleDTO.getDeadline());
        module.setModuleStatus(moduleDTO.getModuleStatus());
        module.setAssignee(studentById);
        module.setReporter(mentorById);

        return moduleRepository.save(module);
    }

    /**
     * Удалить модуль.
     *
     * @param id модуля.
     * @return строка.
     */
    @Override
    public String deleteModule(Long id) {
        moduleRepository.findById(id)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found with id: " + id));

        moduleRepository.deleteById(id);
        return String.format("Module with id: %d was deleted.", id);
    }
}
