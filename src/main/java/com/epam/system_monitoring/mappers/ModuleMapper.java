package com.epam.system_monitoring.mappers;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ModuleMapper {

    ModuleMapper INSTANCE = Mappers.getMapper(ModuleMapper.class);

    ModuleDTO toDTO(Module module);

    Module toModule(ModuleDTO moduleDTO);
}
