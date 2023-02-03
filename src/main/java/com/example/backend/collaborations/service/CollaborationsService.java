package com.example.backend.collaborations.service;

import com.example.backend.collaborations.model.Collaboration;
import com.example.backend.collaborations.model.CollaborationId;
import com.example.backend.collaborations.repo.CollaborationsRepository;
import com.example.backend.companies.model.Company;
import com.example.backend.companies.model.Contact;
import com.example.backend.companies.repo.CompanyRepository;
import com.example.backend.companies.repo.ContactRepository;
import com.example.backend.project.controller.dto.CollaborationDTO;
import com.example.backend.project.model.Project;
import com.example.backend.project.repo.ProjectRepository;
import com.example.backend.user.model.AppUser;
import com.example.backend.user.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CollaborationsService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProjectRepository projectRepository;
    private final CollaborationsRepository collaborationsRepository;
    private final ContactRepository contactRepository;


    public List<Collaboration> getCollaborationsForCompany(Long id) {
        if (companyRepository.findById(id).isEmpty()) throw new EntityNotFoundException();
        return collaborationsRepository.findAllByCollaborationId_Company(companyRepository.findById(id).get());
    }

    public Collaboration addCollaboration(Long projectid, CollaborationDTO collaborationDTO) {
        Project project = projectRepository.findById(projectid).get();
        Company company = companyRepository.findById(collaborationDTO.getCompanyId()).get();
        Contact contact = contactRepository.findById(collaborationDTO.getContactId()).get();
        AppUser responsible = userRepository.findById(collaborationDTO.getContactResponsibleId()).get();

        Collaboration collaboration = new Collaboration(
                new CollaborationId(project, company),
                contact,
                responsible,
                collaborationDTO.isPriority(),
                collaborationDTO.getCategory(),
                collaborationDTO.getStatus(),
                collaborationDTO.getComment(),
                collaborationDTO.getAchievedValue()
        );

        return collaborationsRepository.save(collaboration);
    }

    public Collaboration updateCollaboration(Long projectId, Long companyId, CollaborationDTO collaborationDTO) {
        CollaborationId collaborationId = new CollaborationId(projectRepository.findById(projectId).get(), companyRepository.findById(companyId).get());

        if (!collaborationsRepository.existsById(collaborationId)) return null;
        Collaboration collaboration = collaborationsRepository.findById(collaborationId).get();

        collaboration.setContactResponsible(userRepository.findById(collaborationDTO.getContactResponsibleId()).get());
        collaboration.setContact(contactRepository.findById(collaborationDTO.getContactId()).get());
        collaboration.setPriority(collaborationDTO.isPriority());
        collaboration.setCategories(collaborationDTO.getCategory());
        collaboration.setStatus(collaborationDTO.getStatus());
        collaboration.setComment(collaborationDTO.getComment());
        collaboration.setAchievedValue(collaborationDTO.getAchievedValue());

        return collaborationsRepository.save(collaboration);
    }

    public void deleteCollaboration(Long projectId, Long companyId) {
        CollaborationId collaborationId = new CollaborationId(projectRepository.findById(projectId).get(), companyRepository.findById(companyId).get());
        collaborationsRepository.deleteById(collaborationId);
    }

    public Collaboration getCollaborationByCollaborationId(Long projectid, Long companyid) {
        return collaborationsRepository.findById(new CollaborationId(projectRepository.findById(projectid).get(), companyRepository.findById(companyid).get())).get();
    }
}
