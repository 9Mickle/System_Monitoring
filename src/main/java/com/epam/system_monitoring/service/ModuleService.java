package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;

public interface ModuleService {

    Module getModuleById(Long id);

    Module saveModule(ModuleDTO moduleDTO);

    void deleteModule(Module module);
}
