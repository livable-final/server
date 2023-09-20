package com.livable.server.visitation.mock;

import com.livable.server.visitation.dto.VisitationResponse;

import java.time.LocalDate;
import java.time.LocalTime;

public class MockDetailInformationDto extends VisitationResponse.DetailInformationDto {

    public MockDetailInformationDto() {
        super();
    }

    public MockDetailInformationDto(
            LocalDate invitationStartDate,
            LocalTime invitationStartTime,
            LocalDate invitationEndDate,
            LocalTime invitationEndTime,
            String invitationBuildingName,
            String invitationOfficeName,
            String buildingRepresentativeImageUrl,
            String buildingName,
            String buildingAddress,
            String buildingParkingCostInformation,
            String buildingScale,
            String invitationTip,
            String hostName,
            String hostCompanyName,
            String hostContact,
            String hostBusinessCardImageUrl
    ) {
        super(
                invitationStartDate,
                invitationStartTime,
                invitationEndDate,
                invitationEndTime,
                invitationBuildingName,
                invitationOfficeName,
                buildingRepresentativeImageUrl,
                buildingName,
                buildingAddress,
                buildingParkingCostInformation,
                buildingScale,
                invitationTip,
                hostName,
                hostCompanyName,
                hostContact,
                hostBusinessCardImageUrl
        );
    }
}
