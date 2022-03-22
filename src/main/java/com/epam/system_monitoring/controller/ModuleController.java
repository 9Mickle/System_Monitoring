package com.epam.system_monitoring.controller;

import com.epam.system_monitoring.dto.ModuleDTO;
import com.epam.system_monitoring.mappers.ModuleMapper;
import com.epam.system_monitoring.service.impl.ModuleServiceImpl;
import com.epam.system_monitoring.validation.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/module")
@CrossOrigin
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleServiceImpl moduleService;
    private final ResponseErrorValidation validation;

    @Operation(summary = "This is to fetch all the modules from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch all the modules from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ModuleDTO.class)))
            }),
    })
    @GetMapping("/")
    public ResponseEntity<Object> getAllModule() {
        List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.toDTOList(moduleService.getAllModule());

        return new ResponseEntity<>(moduleDTOList, HttpStatus.OK);
    }


    @Operation(summary = "Get a module by its id from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch module from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ModuleDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Module not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getModule(@PathVariable Long id) {
        ModuleDTO moduleDTO = ModuleMapper.INSTANCE.toDTO(moduleService.getModuleById(id));

        return new ResponseEntity<>(moduleDTO, HttpStatus.OK);
    }


    @Operation(summary = "Get a module by its title from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetch module from Db", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ModuleDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Module not found", content = @Content)
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<Object> getModuleByTitle(@PathVariable String title) {
        ModuleDTO moduleDTO = ModuleMapper.INSTANCE.toDTO(moduleService.getModuleByTitle(title));

        return new ResponseEntity<>(moduleDTO, HttpStatus.OK);
    }


    @Operation(summary = "Create a new module and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "A module has been created", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ModuleDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "When saving the module, there must necessarily be a course and a mentor " +
                            "for which the module will be assigned. " +
                            "It is also not possible to set a start date.", content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Course or mentor or student not found", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createModule(@RequestBody @Valid ModuleDTO moduleDTO,
                                               BindingResult bindingResult) {

        ResponseEntity<Object> errors = validation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        ModuleDTO createdModuleDTO = ModuleMapper.INSTANCE.toDTO(moduleService.saveModule(moduleDTO));

        return new ResponseEntity<>(createdModuleDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Update module and save it to the Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A module has been updated", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ModuleDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Module or student or mentor not found", content = @Content)
    })
    @PutMapping("/update/{id}")
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


    @Operation(summary = "Delete module from Db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A module has been deleted", content = {
                    @Content(
                            mediaType = MediaType.TEXT_HTML_VALUE,
                            examples = @ExampleObject("Module with id was deleted"))}),
            @ApiResponse(responseCode = "404",
                    description = "Module not found", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteModule(@PathVariable Long id) {
        return new ResponseEntity<>(moduleService.deleteModule(id), HttpStatus.OK);
    }
}
