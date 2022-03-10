package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.CourseDTO;
import com.epam.system_monitoring.entity.Course;
import com.epam.system_monitoring.exception.CourseNotFoundException;
import com.epam.system_monitoring.exception.TitleAlreadyExistException;
import com.epam.system_monitoring.mappers.CourseMapper;
import com.epam.system_monitoring.repository.CourseRepository;
import com.epam.system_monitoring.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    /**
     * Получить все курсы.
     *
     * @return лист курсов.
     */
    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Получить курс по id.
     *
     * @param id курса.
     * @return курс.
     */
    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }

    /**
     * Получить курс по названию.
     *
     * @param title название курса.
     * @return курс.
     */
    @Override
    public Course getCourseByTitle(String title) {
        return courseRepository.findByTitle(title)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with title: " + title));
    }

    //todo подумать...

    /**
     * Сохрнаить новый курс.
     *
     * @param courseDTO курс переданный с клиента.
     * @return сохраненный курс.
     */
    @Override
    public Course saveCourse(CourseDTO courseDTO) {
        courseRepository.findByTitle(courseDTO.getTitle())
                .ifPresent(c -> {
                    throw new TitleAlreadyExistException("A course with this title already exists");
                });

        Course course = CourseMapper.INSTANCE.toCourse(courseDTO);
        return courseRepository.save(course);
    }

    /**
     * Обновить курс.
     *
     * @param id        курса.
     * @param courseDTO курс переданный с клиента.
     * @return обновленный курс.
     */
    @Override
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));

        if (!course.getTitle().equals(courseDTO.getTitle())) {
            course.setTitle(courseDTO.getTitle());
            return courseRepository.save(course);
        } else {
            throw new TitleAlreadyExistException("A course with this title already exists");
        }
    }

    /**
     * Удалить курс.
     * При удалении курса удалятся также все модули из БД.
     *
     * @param id курса.
     * @return строка.
     */
    @Override
    public String deleteCourse(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));

        courseRepository.deleteById(id);
        return String.format("Course with id: %d was deleted", id);
    }
}
