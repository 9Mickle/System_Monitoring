package com.epam.system_monitoring.repository;

import com.epam.system_monitoring.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Optional<Mentor> findById(Long id);

    Optional<Mentor> findByUsername(String username);

    Boolean existsByUsername(String username);
}
