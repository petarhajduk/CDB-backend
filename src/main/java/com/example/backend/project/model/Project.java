package com.example.backend.project.model;

import com.example.backend.project.model.enums.TYPE;
import com.example.backend.user.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter @Setter

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "idcreator", nullable = false)
    private Long idCreator;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private TYPE type;

    @Column(name = "startdate", nullable = false)
    private Date startDate;

    @Column(name = "enddate", nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "fr_resp")
    private AppUser FRResp;

    @ManyToMany
    @JoinTable(
            name = "projectfrteammembers",
            joinColumns = @JoinColumn(name = "appuser_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<AppUser> frteammembers;

    @Column(name = "frgoal")
    private Long FRGoal;

    @Column(name = "firstpingdate")
    private Date firstPingDate;

    @Column(name = "secondpingdate")
    private Date secondPingDate;

    public Project(Long idCreator,
                   String name,
                   String category,
                   TYPE type,
                   Date startDate,
                   Date endDate,
                   AppUser FRResp,
                   Set<AppUser> frteammembers,
                   Long FRGoal,
                   Date firstPingDate,
                   Date secondPingDate) {
        this.idCreator = idCreator;
        this.name = name;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.FRResp = FRResp;
        this.frteammembers = frteammembers;
        this.FRGoal = FRGoal;
        this.firstPingDate = firstPingDate;
        this.secondPingDate = secondPingDate;
    }

    public void addFrTeamMember(AppUser user){
        this.frteammembers.add(user);
    }

    public void removeFrTeamMember(AppUser user){
        this.frteammembers.remove(user);
    }
}
