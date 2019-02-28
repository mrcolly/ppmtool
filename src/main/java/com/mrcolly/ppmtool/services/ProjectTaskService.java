package com.mrcolly.ppmtool.services;

import com.mrcolly.ppmtool.domain.Backlog;
import com.mrcolly.ppmtool.domain.ProjectTask;
import com.mrcolly.ppmtool.exceptions.CustomException;
import com.mrcolly.ppmtool.repositories.BacklogRepository;
import com.mrcolly.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProjectTaskService {
	
	private BacklogRepository backlogRepository;
	private ProjectTaskRepository projectTaskRepository;
	private ValidationErrorService validationErrorService;
	
	public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ValidationErrorService validationErrorService) {
		this.backlogRepository = backlogRepository;
		this.projectTaskRepository = projectTaskRepository;
		this.validationErrorService = validationErrorService;
	}
	
	public ProjectTask addProjectTask(String identifier, ProjectTask projectTask, BindingResult result) {
		
		try {
			
			Map errorMap = validationErrorService.MapValidation(result);
			if (errorMap != null) {
				throw new CustomException("object validation error", errorMap);
			}
			
			Backlog backlog = backlogRepository.findByIdentifier(identifier);
			if (backlog == null) {
				throw new CustomException("backlogID " + identifier.toUpperCase() + " not found");
			}
			
			projectTask.setBacklog(backlog);
			
			if (projectTask.getId() != null) {
				throw new CustomException("when you add a new Project you can't pass an id");
			}
			
			backlog.setPTSequence(backlog.getPTSequence() + 1);
			
			projectTask.setSequence(identifier + "-" + backlog.getPTSequence());
			projectTask.setIdentifier(identifier);
			
			if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
				projectTask.setPriority(1);
			}
			
			if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
				projectTask.setStatus("TODO");
			}
			
			return projectTaskRepository.save(projectTask);
			
		} catch (DataAccessException e) {
			Map<String,String> errorMap = new HashMap<>();
			errorMap.put("error", e.getRootCause().getMessage());
			throw new CustomException("SQL exception",errorMap);
		}
		
		
	}
	
	public Backlog findBacklogById(String backlog_id) {
		Backlog backlog = backlogRepository.findByIdentifier(backlog_id);
		
		if (backlog == null) {
			throw new CustomException("backlogID " + backlog_id.toUpperCase() + " not found");
		}
		
		Collections.sort(backlog.getProjectTasks());
		
		return backlog;
	}
	
	public ProjectTask findProjectTaskBySequence(String backlog_id, String sequence) {
		
		ProjectTask projectTask = checkProjectTask(backlog_id, sequence);
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String sequence, BindingResult result) {
		
		try {
			Map errorMap = validationErrorService.MapValidation(result);
			if (errorMap != null) {
				throw new CustomException("object validation error", errorMap);
			}
			
			ProjectTask projectTask = checkProjectTask(updatedTask, backlog_id, sequence);
			
			projectTask = updatedTask;
			
			return projectTaskRepository.save(projectTask);
			
		} catch (DataAccessException e) {
			Map<String,String> errorMap = new HashMap<>();
			errorMap.put("error", e.getRootCause().getMessage());
			throw new CustomException("SQL exception",errorMap);
		}
	}
	
	public void deleteProjectTaskBySequence(String backlog_id, String sequence) {
		
		try {
			
			ProjectTask projectTask = checkProjectTask(backlog_id, sequence);
			projectTaskRepository.delete(projectTask);
			
		} catch (DataAccessException e) {
			Map<String,String> errorMap = new HashMap<>();
			errorMap.put("error", e.getRootCause().getMessage());
			throw new CustomException("SQL exception",errorMap);
		}
	}
	
	
	
	
	private ProjectTask checkProjectTask(String backlog_id, String sequence) {
		Backlog backlog = backlogRepository.findByIdentifier(backlog_id);
		
		if (backlog == null) {
			throw new CustomException("backlogID " + backlog_id.toUpperCase() + " not found");
		}
		
		ProjectTask projectTask = projectTaskRepository.findBySequence(sequence.toUpperCase());
		
		if (projectTask == null) {
			throw new CustomException("projectTask " + sequence.toUpperCase() + " not found");
		}
		
		if (!projectTask.getIdentifier().equals(backlog_id)) {
			throw new CustomException("projectTask " + sequence.toUpperCase() + " is not part of project " + backlog_id);
		}
		
		return projectTask;
	}
	
	private ProjectTask checkProjectTask(ProjectTask updatedTask, String backlog_id, String sequence) {
		Backlog backlog = backlogRepository.findByIdentifier(backlog_id);
		
		if (backlog == null) {
			throw new CustomException("backlogID " + backlog_id.toUpperCase() + " not found");
		}
		
		ProjectTask projectTask = projectTaskRepository.findBySequence(updatedTask.getSequence());
		
		if (projectTask == null) {
			throw new CustomException("projectTask " + updatedTask.getSequence() + " not found");
		}
		
		if (projectTask.getId() != updatedTask.getId()) {
			throw new CustomException("projectTask id" + projectTask.getId() + " different from updated ID");
		}
		
		if (!updatedTask.getSequence().equals(sequence)) {
			throw new CustomException("URL " + updatedTask.getSequence() + " different from updated sequence " + sequence);
		}
		
		if (!projectTask.getIdentifier().equals(backlog_id)) {
			throw new CustomException("projectTask " + updatedTask.getSequence() + " is not part of project " + backlog_id);
		}
		
		return projectTask;
	}
}
