package com.slober.workflow.service;

import com.slober.workflow.dto.ProjectRequest;
import com.slober.workflow.dto.ProjectResponse;
import com.slober.workflow.exception.ResourceNotFoundException;
import com.slober.workflow.model.Project;
import com.slober.workflow.model.User;
import com.slober.workflow.repository.ProjectRepository;
import com.slober.workflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(
        ProjectRepository projectRepository,
        UserRepository userRepository){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponse createProject(ProjectRequest request) {

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + request.getOwnerId() + " not found"
                        )
                );

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(owner);

        Project savedProject = projectRepository.save(project);

        return mapToResponse(savedProject);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project with id " + id + " not found"
                        )
                );

        return mapToResponse(project);
    }

    private ProjectResponse mapToResponse(Project project) {

        ProjectResponse response = new ProjectResponse();

        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setOwnerId(project.getOwner().getId());

        response.setOwnerName(
                project.getOwner().getFirstName()
                        + " "
                        + project.getOwner().getLastName()
        );

        return response;
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request) {

        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project with id " + id + " not found"
                        )
                );

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + request.getOwnerId() + " not found"
                        )
                );

        existingProject.setName(request.getName());
        existingProject.setDescription(request.getDescription());
        existingProject.setOwner(owner);

        Project updatedProject = projectRepository.save(existingProject);

        return mapToResponse(updatedProject);
    }

    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project with id " + id + " not found"
                        )
                );

        projectRepository.delete(project);
    }
}
