package com.example.backend.companies.service;

import com.example.backend.collaborations.model.Collaboration;
import com.example.backend.collaborations.service.CollaborationsService;
import com.example.backend.companies.controller.dto.CompanyDto;
import com.example.backend.companies.controller.dto.ContactDto;
import com.example.backend.companies.model.Company;
import com.example.backend.companies.model.Contact;
import com.example.backend.companies.repo.CompanyRepository;
import com.example.backend.companies.repo.ContactRepository;
import com.example.backend.project.model.Project;
import com.example.backend.user.model.AUTHORITY;
import com.example.backend.user.model.AppUser;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service @Transactional
public class CompanyService
{
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final CollaborationsService collaborationsService;

    public CompanyService(CompanyRepository companyRepository, ContactRepository contactRepository, CollaborationsService collaborationsService)
    {
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
        this.collaborationsService = collaborationsService;
    }

    public List<Company> getAllCompanies(AppUser user) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        return companyRepository.findAll();
    }

    public Company getCompany(AppUser user, Long id) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        if (List.of(AUTHORITY.OBSERVER).contains(user.getAuthority())){
            throw new AuthenticationException();
        }
        if (List.of(AUTHORITY.FR_RESPONSIBLE, AUTHORITY.FR_TEAM_MEMBER).contains(user.getAuthority())){
            if (!isFrTeamMemberOrResponsibleOnCompany(user, id)){
                throw new AuthenticationException();
            }
        }
        Optional<Company> company = companyRepository.findById(id);
        if (!company.isPresent()){
            throw new EntityNotFoundException();
        }
        return company.get();
    }

    public Company createCompany(AppUser user, CompanyDto companyDto) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        if (List.of(AUTHORITY.OBSERVER, AUTHORITY.FR_TEAM_MEMBER).contains(user.getAuthority())){
            throw new AuthenticationException();
        }
        return companyRepository.save(companyDto.toCompany());
    }

    public Company editCompany(AppUser user, Long companyId, CompanyDto companyDto) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        if (List.of(AUTHORITY.OBSERVER, AUTHORITY.FR_TEAM_MEMBER).contains(user.getAuthority())){
            throw new AuthenticationException();
        }
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isEmpty()){
            throw new EntityNotFoundException();
        }
        Company company = optionalCompany.get();
        company.updateWith(companyDto);
        companyRepository.save(company);
        return company;
    }

    public void deleteCompany(AppUser user, Long companyId) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        if (List.of(AUTHORITY.OBSERVER, AUTHORITY.FR_TEAM_MEMBER).contains(user.getAuthority())){
            throw new AuthenticationException();
        }
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isEmpty()){
            throw new EntityNotFoundException();
        }
        companyRepository.deleteCompanyById(companyId);
    }

    public Contact addContactToCompany(AppUser user, Long companyId, ContactDto contactDto) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        if (List.of(AUTHORITY.OBSERVER).contains(user.getAuthority())){
            throw new AuthenticationException();
        }
        if (List.of(AUTHORITY.FR_RESPONSIBLE, AUTHORITY.FR_TEAM_MEMBER).contains(user.getAuthority())){
            if (!isFrTeamMemberOrResponsibleOnCompany(user, companyId)){
                throw new AuthenticationException();
            }
        }

        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isEmpty()){
            throw new EntityNotFoundException();
        }
        Company company = optionalCompany.get();
        Contact contact = contactDto.toContact();
        contact.setCompany(company);
        return contactRepository.save(contact);
    }

    public Contact editContact(AppUser user, Long companyId, Long contactId, ContactDto contactDto) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        if (List.of(AUTHORITY.OBSERVER).contains(user.getAuthority())){
            throw new AuthenticationException();
        }
        if (List.of(AUTHORITY.FR_RESPONSIBLE, AUTHORITY.FR_TEAM_MEMBER).contains(user.getAuthority())){
            if (!isFrTeamMemberOrResponsibleOnCompany(user, companyId)){
                throw new AuthenticationException();
            }
        }
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isEmpty()){
            throw new EntityNotFoundException();
        }
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isEmpty()){
            throw new EntityNotFoundException();
        }
        Contact contact = optionalContact.get();
        if (contact.getCompany().getId() != companyId){
            throw new EntityNotFoundException();
        }
        contact.updateWith(contactDto);
        return contactRepository.save(contact);
    }

    public void deleteContact(AppUser user, Long companyId, Long contactId) throws AuthenticationException
    {
        if (user == null) throw new AuthenticationException();
        if (List.of(AUTHORITY.OBSERVER).contains(user.getAuthority())){
            throw new AuthenticationException();
        }
        if (List.of(AUTHORITY.FR_RESPONSIBLE, AUTHORITY.FR_TEAM_MEMBER).contains(user.getAuthority())){
            if (!isFrTeamMemberOrResponsibleOnCompany(user, companyId)){
                throw new AuthenticationException();
            }
        }

        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isEmpty()){
            throw new EntityNotFoundException();
        }
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isEmpty()){
            throw new EntityNotFoundException();
        }
        Contact contact = optionalContact.get();
        if (contact.getCompany().getId() != companyId){
            throw new EntityNotFoundException();
        }
        contactRepository.deleteContactById(contactId);
    }

    private boolean isFrTeamMemberOrResponsibleOnCompany(AppUser user, Long companyId){
        List<Collaboration> collaborations = collaborationsService.getCollaborationsForCompany(companyId);
        for(Collaboration collaboration : collaborations){
            Project project = collaboration.getCollaborationId().getProject();
            Set<AppUser> frTeamMembers = project.getFrteammembers();
            if (frTeamMembers.contains(user) || project.getFRResp().getId() == user.getId()){
                return true;
            }
        }
        return false;
    }

    public boolean existsById(Long companyid) {
        return companyRepository.existsById(companyid);
    }
}
