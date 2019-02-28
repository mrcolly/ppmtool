package com.mrcolly.ppmtool.bootstrap;

import com.mrcolly.ppmtool.domain.Project;
import com.mrcolly.ppmtool.services.ProjectService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private ProjectService projectService;

    public Bootstrap(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void run(String... args) throws Exception {

        /*for(int i = 0; i<5; i++){
            Project p = new Project("prova", "PRO"+i, "prova", null, null);
            projectService.SaveOrUpdate(p);
        }

        System.out.println("bootstrapping complete");*/

    }
}
