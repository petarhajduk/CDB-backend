package com.example.backend.companies.repo;

import com.example.backend.companies.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAll();

    List<Company> deleteCompanyById(Long id);
}
