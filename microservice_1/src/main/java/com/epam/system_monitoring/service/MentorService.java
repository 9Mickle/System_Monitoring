package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.MentorDTO;
import com.epam.system_monitoring.entity.Mentor;

import java.util.List;

public interface MentorService {

    List<Mentor> getAllMentors();

    Mentor getMentorById(Long id);

    Mentor updateMentor(Long mentorId, MentorDTO mentorDTO);

    Mentor saveMentor(MentorDTO mentorDTO);

    Mentor addStudent(Long mentorId, Long studentId);

    String deleteMentor(Long id);
}
