package com.livable.server.visitation.dto;

import com.livable.server.invitation.dto.InvitationDetailTimeDto;
import com.livable.server.visitation.domain.PlaceType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitationResponse {

    @Getter
    @NoArgsConstructor
    public static class Base64QrCode {

        private String qr;

        public static Base64QrCode of(String base64QrCode) {
            Base64QrCode code = new Base64QrCode();
            code.qr = base64QrCode;
            return code;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DetailInformationDto {

        private LocalDate invitationStartDate;
        private LocalTime invitationStartTime;
        private LocalDate invitationEndDate;
        private LocalTime invitationEndTime;
        private String invitationBuildingName;
        private String invitationOfficeName;

        private String buildingRepresentativeImageUrl;
        private String buildingName;
        private String buildingAddress;
        private String buildingParkingCostInformation;
        private String buildingScale;

        private PlaceType placeType;
        private String invitationTip;

        private String hostName;
        private String hostCompanyName;
        private String hostContact;
        private String hostBusinessCardImageUrl;

        public DetailInformationDto(LocalDate invitationStartDate, LocalTime invitationStartTime, LocalDate invitationEndDate, LocalTime invitationEndTime, String invitationBuildingName, String invitationOfficeName, String buildingRepresentativeImageUrl, String buildingName, String buildingAddress, String buildingParkingCostInformation, String buildingScale, String placeType, String invitationTip, String hostName, String hostCompanyName, String hostContact, String hostBusinessCardImageUrl) {
            this.invitationStartDate = invitationStartDate;
            this.invitationStartTime = invitationStartTime;
            this.invitationEndDate = invitationEndDate;
            this.invitationEndTime = invitationEndTime;
            this.invitationBuildingName = invitationBuildingName;
            this.invitationOfficeName = invitationOfficeName;
            this.buildingRepresentativeImageUrl = buildingRepresentativeImageUrl;
            this.buildingName = buildingName;
            this.buildingAddress = buildingAddress;
            this.buildingParkingCostInformation = buildingParkingCostInformation;
            this.buildingScale = buildingScale;
            this.placeType = PlaceType.valueOf(placeType);
            this.invitationTip = invitationTip;
            this.hostName = hostName;
            this.hostCompanyName = hostCompanyName;
            this.hostContact = hostContact;
            this.hostBusinessCardImageUrl = hostBusinessCardImageUrl;
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InvitationTimeDto {
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;

        public LocalDateTime getStartDateTime() {
            return LocalDateTime.of(startDate, startTime);
        }

        public LocalDateTime getEndDateTime() {
            return LocalDateTime.of(endDate, endTime);
        }

        public static InvitationTimeDto from(final InvitationDetailTimeDto invitationDetailTimeDto) {
            return InvitationTimeDto.builder()
                    .startTime(invitationDetailTimeDto.getStartTime())
                    .endTime(invitationDetailTimeDto.getEndTime())
                    .startDate(invitationDetailTimeDto.getStartDate())
                    .endDate(invitationDetailTimeDto.getEndDate())
                    .build();
        }
    }
}
