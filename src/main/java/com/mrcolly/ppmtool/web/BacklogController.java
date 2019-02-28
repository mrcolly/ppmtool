package com.mrcolly.ppmtool.web;


import com.mrcolly.ppmtool.domain.Backlog;
import com.mrcolly.ppmtool.domain.ProjectTask;
import com.mrcolly.ppmtool.domain.Response;
import com.mrcolly.ppmtool.exceptions.CustomException;
import com.mrcolly.ppmtool.services.ProjectTaskService;
import com.mrcolly.ppmtool.services.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    private ProjectTaskService projectTaskService;
    private ValidationErrorService validationErrorService;

    public BacklogController(ProjectTaskService projectTaskService, ValidationErrorService validationErrorService) {
        this.projectTaskService = projectTaskService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result,
                                            @PathVariable String backlog_id){

        ProjectTask projectTaskResp = projectTaskService.addProjectTask(backlog_id, projectTask, result);

        return new ResponseEntity<ProjectTask>(projectTaskResp, HttpStatus.CREATED);

    }

    @GetMapping("/{backlog_id}")
    public ResponseEntity<?> getProjectTasks(@PathVariable String backlog_id){

        Backlog backlog = projectTaskService.findBacklogById(backlog_id);

        return new ResponseEntity<Backlog>(backlog, HttpStatus.CREATED);

    }

    @GetMapping("/{backlog_id}/{pt_sequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_sequence ){

        ProjectTask projectTask = projectTaskService.findProjectTaskBySequence(backlog_id, pt_sequence);

        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);

    }

    @PatchMapping("/{backlog_id}/{pt_sequence}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result,
                                            @PathVariable String backlog_id,
                                            @PathVariable String pt_sequence){

        Map errorMap = validationErrorService.MapValidation(result);
        if(errorMap!=null) {
            throw new CustomException("object validation error", errorMap);
        }

        ProjectTask projectTaskResp = projectTaskService.updateByProjectSequence(projectTask, backlog_id, pt_sequence, result);

        return new ResponseEntity<ProjectTask>(projectTaskResp, HttpStatus.CREATED);

    }
    
    @DeleteMapping("/{backlog_id}/{pt_sequence}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id,
                                               @PathVariable String pt_sequence){
        projectTaskService.deleteProjectTaskBySequence(backlog_id,pt_sequence);
        
        return new ResponseEntity<Response>(new Response("ProjectTask "+pt_sequence+" deleted successfully"), HttpStatus.OK);
    }
}
