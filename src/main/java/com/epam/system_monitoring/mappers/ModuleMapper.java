package com.epam.system_monitoring.mappers;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ModuleMapper {

    ModuleMapper INSTANCE = Mappers.getMapper(ModuleMapper.class);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "course.id", target = "courseId")
    ModuleDTO toDTO(Module module);

    List<ModuleDTO> toDTOList(List<Module> modules);

    Module toModule(ModuleDTO moduleDTO);
}
