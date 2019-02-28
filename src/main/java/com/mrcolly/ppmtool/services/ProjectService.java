package com.mrcolly.ppmtool.services;

import com.mrcolly.ppmtool.domain.Backlog;
import com.mrcolly.ppmtool.domain.Project;
import com.mrcolly.ppmtool.exceptions.CustomException;
import com.mrcolly.ppmtool.repositories.BacklogRepository;
import com.mrcolly.ppmtool.repositories.ProjectRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProjectService {
	
	private ProjectRepository projectRepository;
	private BacklogRepository backlogRepository;
	private ValidationErrorService validationErrorService;
	
	public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository, ValidationErrorService validationErrorService) {
		this.projectRepository = projectRepository;
		this.backlogRepository = backlogRepository;
		this.validationErrorService = validationErrorService;
	}
	
	public Project SaveOrUpdate(Project project, BindingResult result) {
		
		try {
			Map errorMap = validationErrorService.MapValidation(result);
			if (errorMap != null) {
				throw new CustomException("object validation error", errorMap);
			}
			
			if (project.getStartDate() == null) {
				project.setStartDate(new Date());
			}
			project.setIdentifier(project.getIdentifier().toUpperCase());
			
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setIdentifier(project.getIdentifier().toUpperCase());
			} else {
				project.setBacklog(backlogRepository.findByIdentifier(project.getIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(project);
			
		} catch (DataAccessException e) {
			Map<String,String> errorMap = new HashMap<>();
			errorMap.put("error", e.getRootCause().getMessage());
			throw new CustomException("SQL exception",errorMap);
		}
		
		
	}
	
	public Project findProjectByIdentifier(String identifier) {
		Project project = projectRepository.findByIdentifier(identifier.toUpperCase());
		
		if (project == null) {
			throw new CustomException("Project ID " + identifier.toUpperCase() + " not found");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectBiIdentifier(String identifier) {
		Project project = projectRepository.findByIdentifier(identifier.toUpperCase());
		
		if (project == null) throw new CustomException("cannot delete " + identifier.toUpperCase() + " project");
		
		projectRepository.deleteById(project.getId());
	}
	
	
}
