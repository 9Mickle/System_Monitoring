package com.epam.system_monitoring.service;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;

import java.util.List;

public interface ModuleService {

    List<Module> getAllModule();

    Module getModuleById(Long id);

    Module getModuleByTitle(String title);

    Module updateModule(Long moduleId, ModuleDTO moduleDTO);

    Module saveModule(ModuleDTO moduleDTO);

    String deleteModule(Long id);
}
