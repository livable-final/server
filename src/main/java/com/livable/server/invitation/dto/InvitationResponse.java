package com.livable.server.invitation.dto;

import com.livable.server.entity.Invitation;
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
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;

        public static CommonPlaceDTO from(InvitationProjection.ReservationDTO reservationDTO) {
            return new CommonPlaceDTO(
                    reservationDTO.getCommonPlaceId(),
                    getFormattedPlaceName(
                            reservationDTO.getCommonPlaceName(),
                            reservationDTO.getCommonPlaceFloor(),
                            reservationDTO.getCommonPlaceRoomNumber()
                    ),
                    reservationDTO.getReservationDate(),
                    reservationDTO.getReservationStartTime(),
                    reservationDTO.getReservationEndTime()
            );
        }
    }

    private static String getFormattedPlaceName(String name, String floor, String roomNumber) {
        return String.format("%s (%s층 %s호)", name, floor, roomNumber);
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

}
