package com.mrcolly.ppmtool.web;

import com.mrcolly.ppmtool.domain.Project;
import com.mrcolly.ppmtool.domain.Response;
import com.mrcolly.ppmtool.services.ProjectService;
import com.mrcolly.ppmtool.services.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/project")
@CrossOrigin
public class ProjectController {

    private ProjectService projectService;
    private ValidationErrorService validationErrorService;

    public ProjectController(ProjectService projectService, ValidationErrorService validationErrorService) {
        this.projectService = projectService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrUpdateProject(@Valid @RequestBody Project project, BindingResult result){

        Project p = projectService.SaveOrUpdate(project, result);
        return new ResponseEntity(p, HttpStatus.CREATED);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<?> getProjectByIdentifier(@PathVariable String identifier){

        Project project = projectService.findProjectByIdentifier(identifier);
        return new ResponseEntity(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects(){

        Iterable<Project> projects = projectService.findAllProjects();
        return new ResponseEntity(projects, HttpStatus.OK);
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteProject(@PathVariable String identifier){
        projectService.deleteProjectBiIdentifier(identifier);
        return new ResponseEntity<>(new Response("project "+identifier+" deleted successfully"), HttpStatus.OK);
    }

}
