package com.example.backend.companies.model;

import javax.persistence.*;

@Entity
public class Town
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long zipCode;

    @Column(nullable = false, name = "townName", length = 120, unique = true)
    private String townName;

    public Town()
    {
    }

    public Town(Long zipCode, String townName)
    {
        this.zipCode = zipCode;
        this.townName = townName;
    }

    public Long getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(Long zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getTownName()
    {
        return townName;
    }

    public void setTownName(String townName)
    {
        this.townName = townName;
    }
}
