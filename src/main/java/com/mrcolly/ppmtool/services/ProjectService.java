package com.mrcolly.ppmtool.services;

import com.mrcolly.ppmtool.domain.Project;
import com.mrcolly.ppmtool.exceptions.ProjectIdentifierException;
import com.mrcolly.ppmtool.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project SaveOrUpdate(Project project){

        try {

            if(project.getStartDate() == null){
                project.setStartDate(new Date());
            }

            project.setIdentifier(project.getIdentifier().toUpperCase());
            return projectRepository.save(project);

        }catch (Exception e){
            throw new ProjectIdentifierException("Project ID "+ project.getIdentifier()+" already exist");
        }
    }


}
