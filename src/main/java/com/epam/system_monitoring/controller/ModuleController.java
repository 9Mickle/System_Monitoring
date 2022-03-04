package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.service.impl.ModuleServiceImpl;
import com.epam.system_monitoring.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/module")
@CrossOrigin
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleServiceImpl moduleService;
    private final ResponseErrorValidation validation;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getModule(@PathVariable Long id) {
        ModuleDTO moduleDTO = ModuleMapper.INSTANCE.toDTO(moduleService.getModuleById(id));

        return new ResponseEntity<>(moduleDTO, HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Object> getModuleByTitle(@PathVariable String title) {
        ModuleDTO moduleDTO = ModuleMapper.INSTANCE.toDTO(moduleService.getModuleByTitle(title));

        return new ResponseEntity<>(moduleDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createModule(@RequestBody @Valid ModuleDTO moduleDTO,
                                               BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        ModuleDTO createdModuleDTO = ModuleMapper.INSTANCE.toDTO(moduleService.saveModule(moduleDTO));

        return new ResponseEntity<>(createdModuleDTO, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateModule(@PathVariable Long id,
                                               @RequestBody @Valid ModuleDTO moduleDTO,
                                               BindingResult bindingResult) {
        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        ModuleDTO updatedModuleDTO = ModuleMapper.INSTANCE.
                toDTO(moduleService.updateModule(id, moduleDTO));

        return new ResponseEntity<>(updatedModuleDTO, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteModule(@PathVariable Long id) {
        return new ResponseEntity<>(moduleService.deleteModule(id), HttpStatus.OK);
    }
}
