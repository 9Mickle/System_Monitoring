package com.epam.system_monitoring.service.impl;

import com.epam.system_monitoring.entity.ModuleStatus;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.service.CheckModuleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Фоновый сервис для проверки дедлайна задачи.
 * Если крайний срок истёк, то статус задачи переводится в OVERDUE.
 */
@Component
@RequiredArgsConstructor
public class CheckModuleStatusImpl implements CheckModuleStatus {

    private final ModuleServiceImpl moduleService;

    @Override
    @Scheduled(initialDelayString = "${initial.delay}", fixedDelayString = "${fixed.delay}")
    public void checkStatus() {
        LocalDateTime today = LocalDateTime.now();

        moduleService.getAllModule()
                .stream()
                .filter(module -> module.getDeadline() != null)
                .filter(module -> (module.getModuleStatus() != ModuleStatus.OVERDUE
                        && module.getModuleStatus() == ModuleStatus.INPROGRESS))
                .forEach(module -> {
                    LocalDateTime deadline = module.getDeadline();
                    if (deadline.isBefore(today)) {
                        module.setModuleStatus(ModuleStatus.OVERDUE);
                        moduleService.updateModule(module.getId(), ModuleMapper.INSTANCE.toDTO(module));
                    }
                });
    }
}
