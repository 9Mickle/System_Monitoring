package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.exception.ModuleNotFoundException;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.repository.ModuleRepository;
import com.epam.system_monitoring.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;

    @Override
    @Transactional
    public Module getModuleById(Long id) {
        Optional<Module> module = moduleRepository.findById(id);
        if (module.isPresent()) {
            return module.get();
        } else {
            throw new ModuleNotFoundException("Module not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public Module saveModule(ModuleDTO moduleDTO) {
        return moduleRepository.save(ModuleMapper.INSTANCE.toModule(moduleDTO));
    }

    @Override
    @Transactional
    public void deleteModule(Long id) {
        Optional<Module> module = moduleRepository.findById(id);
        if (module.isPresent()) {
            moduleRepository.delete(module.get());
        } else {
            throw new ModuleNotFoundException("Module not found with id: " + id);
        }
    }
}
