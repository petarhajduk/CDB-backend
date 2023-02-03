package com.example.backend.companies.controller.dto;

import com.example.backend.companies.model.Company;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompanyDto
{
    private String name;

    private String domain;

    private char abcCategory;

    private String budgetPlanningMonth;

    private String country;

    private int zipCode;

    private String address;

    private String webUrl;

    private boolean contactInFuture;

    private String description;

    public CompanyDto(String name, String domain, char abcCategory, String budgetPlanningMonth, String country, int zipCode, String address, String webUrl, boolean contactInFuture, String description) {
        this.name = name;
        this.domain = domain;
        this.abcCategory = abcCategory;
        this.budgetPlanningMonth = budgetPlanningMonth;
        this.country = country;
        this.zipCode = zipCode;
        this.address = address;
        this.webUrl = webUrl;
        this.contactInFuture = contactInFuture;
        this.description = description;
    }

    public Company toCompany(){
        return new Company(
                name,
                domain,
                abcCategory,
                budgetPlanningMonth,
                country,
                zipCode,
                address,
                webUrl,
                contactInFuture,
                description
        );
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public char getAbcCategory()
    {
        return abcCategory;
    }

    public void setAbcCategory(char abcCategory)
    {
        this.abcCategory = abcCategory;
    }

    public String getBudgetPlanningMonth()
    {
        return budgetPlanningMonth;
    }

    public void setBudgetPlanningMonth(String budgetPlanningMonth)
    {
        this.budgetPlanningMonth = budgetPlanningMonth;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public int getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(int zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getWebUrl()
    {
        return webUrl;
    }

    public void setWebUrl(String webUrl)
    {
        this.webUrl = webUrl;
    }

    public boolean isContactInFuture()
    {
        return contactInFuture;
    }

    public void setContactInFuture(boolean contactInFuture)
    {
        this.contactInFuture = contactInFuture;
    }
}
