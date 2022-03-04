package com.epam.system_monitoring.mappers;

import com.epam.system_monitoring.dto.StudentDTO;
import com.epam.system_monitoring.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDTO toDTO(Student student);

    Student toStudent(StudentDTO studentDTO);
}
