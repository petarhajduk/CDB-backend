package com.example.backend.project.service;

import com.example.backend.collaborations.model.Collaboration;
import com.example.backend.collaborations.repo.CollaborationsRepository;
import com.example.backend.project.controller.dto.ProjectDTO;
import com.example.backend.project.model.Project;
import com.example.backend.project.repo.ProjectRepository;
import com.example.backend.user.model.AUTHORITY;
import com.example.backend.user.model.AppUser;
import com.example.backend.user.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service @Transactional
@AllArgsConstructor
public class ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private CollaborationsRepository collaborationsRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).get();
    }

    public Project addProject(ProjectDTO projectDTO) {
        AppUser user = userRepository.findById(projectDTO.getIdFRResp()).get();
        if (user.getAuthority() == AUTHORITY.OBSERVER || user.getAuthority() == AUTHORITY.FR_TEAM_MEMBER){
            user.setAuthority(AUTHORITY.FR_RESPONSIBLE);
            userRepository.save(user);
        }
        Project project = new Project(
                projectDTO.getIdCreator(),
                projectDTO.getName(),
                projectDTO.getCategory(),
                projectDTO.getType(),
                projectDTO.getStartDate(),
                projectDTO.getEndDate(),
                user,
                new HashSet<>(),
                projectDTO.getFRgoal(),
                projectDTO.getFirstPingDate(),
                projectDTO.getSecondPingDate()
        );
        project.addFrTeamMember(user);
        return projectRepository.save(project);
    }

    public Project updateProject(ProjectDTO projectDTO, Long id) {
        Project project = projectRepository.findById(id).get();

        project.setIdCreator(projectDTO.getIdCreator());
        project.setName(projectDTO.getName());
        project.setCategory(projectDTO.getCategory());
        project.setType(projectDTO.getType());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setFRResp(userRepository.findById(projectDTO.getIdFRResp()).get());
        project.setFRGoal(projectDTO.getFRgoal());
        project.setFirstPingDate(projectDTO.getFirstPingDate());
        project.setSecondPingDate(projectDTO.getSecondPingDate());

        return projectRepository.save(project);
    }

    public List<Collaboration> getCollaborations(Long id) {
        Project project = projectRepository.findById(id).get();
        return collaborationsRepository.findAllByCollaborationId_Project(project);
    }

    public void addFrTeamMember(Long projectId, Long teamMemberId){
        Project project = projectRepository.findById(projectId).get();
        AppUser user = userRepository.findById(teamMemberId).get();
        if (user.getAuthority() == AUTHORITY.OBSERVER) {
            user.setAuthority(AUTHORITY.FR_TEAM_MEMBER);
            userRepository.save(user);
        }
        project.addFrTeamMember(user);
        projectRepository.save(project);
    }

    public void deleteFrTeamMember(Long projectId, Long teamMemberId){
        Project project = projectRepository.findById(projectId).get();
        AppUser user = userRepository.findById(teamMemberId).get();
        project.removeFrTeamMember(user);
        if (user.getProjects().isEmpty() && user.getAuthority() == AUTHORITY.FR_TEAM_MEMBER){
            user.setAuthority(AUTHORITY.OBSERVER);
            userRepository.save(user);
        }
        projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return projectRepository.existsById(id);
    }
}
