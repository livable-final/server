package com.livable.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessCardProjection {

    String buildingName;
    String employeeNumber;
    String companyName;
    String floor;
    String roomNumber;
    String employeeName;

}
