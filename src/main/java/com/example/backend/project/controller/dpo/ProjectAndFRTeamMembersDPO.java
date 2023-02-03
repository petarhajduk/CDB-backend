package com.example.backend.project.controller.dpo;

import com.example.backend.project.model.enums.TYPE;
import com.example.backend.user.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@AllArgsConstructor
@Getter @Setter
public class ProjectAndFRTeamMembersDPO {
    private Long id;
    private Long idCreator;
    private String name;
    private String category;
    private TYPE type;
    private Date startDate;
    private Date endDate;
    private AppUser FRResp;
    private Long FRGoal;
    private Date firstPingDate;
    private Date secondPingDate;
    private Set<FRTeamMemberDPO> frTeamMembers;
}
