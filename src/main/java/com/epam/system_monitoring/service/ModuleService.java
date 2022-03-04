package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;

public interface ModuleService {

    Module getModuleById(Long id);

    Module getModuleByTitle(String title);

    Module updateModule(Long moduleId, ModuleDTO moduleDTO);

    Module saveModule(ModuleDTO moduleDTO);

    String deleteModule(Long id);
}
