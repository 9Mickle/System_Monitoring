package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.configuration.RabbitMQConfig;
import com.epam.system_monitoring.dto.MentorDTO;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.entity.*;
import com.epam.system_monitoring.exception.data.DataCannotBeChangedException;
import com.epam.system_monitoring.exception.found.CourseNotFoundException;
import com.epam.system_monitoring.exception.found.MentorNotFoundException;
import com.epam.system_monitoring.exception.found.ModuleNotFoundException;
import com.epam.system_monitoring.exception.found.StudentNotFoundException;
import com.epam.system_monitoring.mappers.MentorMapper;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.repository.CourseRepository;
import com.epam.system_monitoring.repository.MentorRepository;
import com.epam.system_monitoring.repository.ModuleRepository;
import com.epam.system_monitoring.repository.StudentRepository;
import com.epam.system_monitoring.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final CourseRepository courseRepository;

    private final RabbitTemplate template;

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
        return moduleRepository.findByTitle(title)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found with title: " + title));
    }

    @Override
    @Transactional
    public Module saveModule(ModuleDTO moduleDTO) {

        if (moduleDTO.getStartDate() != null) {
            throw new DataCannotBeChangedException("Start date cannot be changed!");
        }

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

        MentorDTO mentorDTO = MentorMapper.INSTANCE.toDTO(mentor);
        //Отправляем данные ментора и статус модуля в очередь.
        sendMapToQueue(mentorDTO, moduleDTO.getModuleStatus());

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

        Student student = studentRepository.findById(moduleDTO.getAssigneeId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + moduleDTO.getAssigneeId()));

        Mentor mentor = mentorRepository.findById((moduleDTO.getReporterId()))
                .orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + moduleDTO.getReporterId()));

        //Если статус модуля изменился, то отправляем данные ментора и статус модуля в очередь.
        if (!module.getModuleStatus().equals(moduleDTO.getModuleStatus())) {
            MentorDTO mentorDTO = MentorMapper.INSTANCE.toDTO(mentor);
            sendMapToQueue(mentorDTO, moduleDTO.getModuleStatus());
        }

        module.setTitle(moduleDTO.getTitle());
        module.setDescription(moduleDTO.getDescription());
        module.setDeadline(moduleDTO.getDeadline());
        module.setModuleStatus(moduleDTO.getModuleStatus());
        module.setAssignee(student);
        module.setReporter(mentor);

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

    private void sendMapToQueue(MentorDTO mentorDTO, ModuleStatus status) {
        Map<String, String> map = new HashMap<>();

        if (mentorDTO.getPhoneNumber() != null) {
            map.put("phoneNumber", mentorDTO.getPhoneNumber());
        }
        map.put("email", mentorDTO.getEmail());
        map.put("moduleStatus", status.toString());

        // Конвертируем и отправляем данные в очередь, из неё другой микросервис получит эти значения.
        template.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                map);
    }
}
