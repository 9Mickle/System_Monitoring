package com.epam.system_monitoring.mappers;

import com.epam.system_monitoring.dto.MentorDTO;
import com.epam.system_monitoring.entity.Mentor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MentorMapper {

    MentorMapper INSTANCE = Mappers.getMapper(MentorMapper.class);

    MentorDTO toDTO(Mentor mentor);

    Mentor toMentor(MentorDTO mentorDTO);
}
