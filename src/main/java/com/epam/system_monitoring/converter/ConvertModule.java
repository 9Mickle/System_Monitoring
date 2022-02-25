package com.epam.system_monitoring.converter;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;
import org.springframework.stereotype.Component;

@Component
public class ConvertModule {

    public static ModuleDTO moduleToModuleDTO(Module module) {
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setTitle(module.getTitle());
        moduleDTO.setDescription(module.getDescription());
        moduleDTO.setModuleStatus(module.getModuleStatus());
        moduleDTO.setColor(module.getColor());
        moduleDTO.setTime(module.getTime());
        return moduleDTO;
    }

    public Module convertDTOToModule(ModuleDTO moduleDTO) {
        Module module = new Module();
        module.setTitle(moduleDTO.getTitle());
        module.setDescription(moduleDTO.getDescription());
        module.setColor(moduleDTO.getColor());
        module.setTime(moduleDTO.getTime());
        module.setAssignee(moduleDTO.getAssignee());
        module.setReporter(moduleDTO.getReporter());
        return module;
    }
}
