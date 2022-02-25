package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.entity.Module;
import com.epam.system_monitoring.impl.ModuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/module")
@CrossOrigin
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleServiceImpl moduleService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getModule(@PathVariable Long id) {
        return new ResponseEntity<>(moduleService.getModuleById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createModule(@RequestBody @Valid ModuleDTO moduleDTO) {
        return new ResponseEntity<>(moduleService.saveModule(moduleDTO), HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteModule(@PathVariable Long id) {
        Module module = moduleService.getModuleById(id);
        moduleService.deleteModule(module);

        return new ResponseEntity<>(String.format("Module with id: %d was deleted", id), HttpStatus.OK);
    }

}
