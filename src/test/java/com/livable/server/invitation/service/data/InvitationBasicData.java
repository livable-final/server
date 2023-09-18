package com.livable.server.invitation.service.data;

import com.livable.server.entity.Building;
import com.livable.server.entity.Company;
import com.livable.server.entity.Member;
import com.livable.server.entity.Office;

import java.util.List;

public class InvitationBasicData {

    private static InvitationBasicData instance = null;

    public static InvitationBasicData getInstance() {
        if (instance == null) {
            instance = new InvitationBasicData();
        }
        return instance;
    }

    private InvitationBasicData() {
        this.building = Building.builder()
                .id(1L)
                .build();

        this.company = Company.builder()
                .id(1L)
                .building(building)
                .build();

        this.member = Member.builder()
                .id(1L)
                .company(company)
                .build();

        this.offices = List.of(
                Office.builder().id(1L).company(company).build(),
                Office.builder().id(2L).company(company).build(),
                Office.builder().id(3L).company(company).build()
        );
    }

    private final Building building;
    private final Company company;
    private final Member member;
    private final List<Office> offices;

    public Building getBuilding() {
        return building;
    }

    public Company getCompany() {
        return company;
    }

    public Member getMember() {
        return member;
    }

    public List<Office> getOffices() {
        return offices;
    }
}
