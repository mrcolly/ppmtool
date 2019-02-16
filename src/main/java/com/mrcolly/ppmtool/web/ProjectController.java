package com.mrcolly.ppmtool.web;

import com.mrcolly.ppmtool.domain.Project;
import com.mrcolly.ppmtool.services.ProjectService;
import com.mrcolly.ppmtool.services.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/project")
public class ProjectController {

    private ProjectService projectService;
    private ValidationErrorService validationErrorService;

    public ProjectController(ProjectService projectService, ValidationErrorService validationErrorService) {
        this.projectService = projectService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult bindingResult){

        ResponseEntity<?> errorMap = validationErrorService.MapValidation(bindingResult);
        if(errorMap!=null) return errorMap;

        Project p = projectService.SaveOrUpdate(project);
        return new ResponseEntity(p, HttpStatus.CREATED);
    }
}
