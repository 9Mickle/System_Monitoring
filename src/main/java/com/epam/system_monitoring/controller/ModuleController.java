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

    @PostMapping("/create")
    public ResponseEntity<Object> createModule(@RequestBody @Valid ModuleDTO moduleDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        ModuleDTO createdModuleDto = ModuleMapper.INSTANCE.toDTO(moduleService.saveModule(moduleDTO));

        return new ResponseEntity<>(createdModuleDto, HttpStatus.CREATED);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);

        return new ResponseEntity<>(String.format("Module with id: %d was deleted", id), HttpStatus.OK);
    }

}
