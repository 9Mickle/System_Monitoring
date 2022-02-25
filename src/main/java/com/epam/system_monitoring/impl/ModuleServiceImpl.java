package com.epam.system_monitoring.impl;

import com.epam.system_monitoring.converter.ConvertModule;
import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.repository.ModuleRepository;
import com.epam.system_monitoring.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final ConvertModule convertModule;

    @Override
    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).get();
    }

    @Override
    public Module saveModule(ModuleDTO moduleDTO) {
        return moduleRepository.save(convertModule.convertDTOToModule(moduleDTO));
    }

    @Override
    public void deleteModule(Module module) {
        moduleRepository.delete(module);
    }
}
