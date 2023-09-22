package com.livable.server.invitation.dto;

import com.livable.server.entity.Office;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvitationResponse {


    @Getter
    @Builder
    public static class AvailablePlacesDTO {
        private List<OfficeDTO> offices;
        private List<CommonPlaceDTO> commonPlaces;
    }

    @Getter
    @Builder
    public static class OfficeDTO {
        private String officeName;

        public static OfficeDTO from(Office office) {
            return new OfficeDTO(getFormattedPlaceName(
                    office.getName(),
                    office.getFloor(),
                    office.getRoomNumber()
            ));
        }
    }

    @Getter
    @Builder
    public static class CommonPlaceDTO {
        private Long commonPlaceId;
        private String commonPlaceName;

        public static CommonPlaceDTO from(ReservationDTO reservationDTO) {
            return new CommonPlaceDTO(
                    reservationDTO.getCommonPlaceId(),
                    getFormattedPlaceName(
                            reservationDTO.getCommonPlaceName(),
                            reservationDTO.getCommonPlaceFloor(),
                            reservationDTO.getCommonPlaceRoomNumber()
                    )
            );
        }
    }

    private static String getFormattedPlaceName(String name, String floor, String roomNumber) {
        return String.format("%s (%s층 %s호)", name, floor, roomNumber);
    }

    @Getter
    @Setter
    public static class ReservationDTO {
        private Long commonPlaceId;
        private String commonPlaceFloor;
        private String commonPlaceRoomNumber;
        private String commonPlaceName;

        public ReservationDTO(
                Long commonPlaceId,
                String commonPlaceFloor,
                String commonPlaceRoomNumber,
                String commonPlaceName
        ) {
            this.commonPlaceId = commonPlaceId;
            this.commonPlaceFloor = commonPlaceFloor;
            this.commonPlaceRoomNumber = commonPlaceRoomNumber;
            this.commonPlaceName = commonPlaceName;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ListDTO {
        private Long invitationId;
        private String visitorName;
        private Long visitorCount;
        private String purpose;
        private String officeName;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalTime endTime;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DetailDTO {
        private Long commonPlaceId;
        private String officeName;
        private String purpose;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private List<VisitorForDetailDTO> visitors;

        public DetailDTO(
                Long commonPlaceId,
                String officeName,
                String purpose,
                String description,
                LocalDate startDate,
                LocalDate endDate,
                LocalTime startTime,
                LocalTime endTime
        ) {
            this.commonPlaceId = commonPlaceId;
            this.officeName = officeName;
            this.purpose = purpose;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public void setVisitors(List<VisitorForDetailDTO> visitors) {
            this.visitors = visitors;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class VisitorForDetailDTO {
        private Long visitorId;
        private String name;
        private String contact;
    }

}
