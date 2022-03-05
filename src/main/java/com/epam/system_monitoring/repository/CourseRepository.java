package com.epam.system_monitoring.repository;

import com.epam.system_monitoring.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findById(Long id);

    Optional<Course> findByTitle(String title);

    Boolean existsByTitle(String title);

}
