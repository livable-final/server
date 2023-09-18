package com.livable.server.invitation.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvitationProjection {

    @Getter
    @Setter
    public static class ReservationDTO {
        private Long commonPlaceId;
        private String commonPlaceFloor;
        private String commonPlaceRoomNumber;
        private String commonPlaceName;
        private LocalDate reservationDate;
        private LocalTime reservationStartTime;
        private LocalTime reservationEndTime;

        public ReservationDTO(
                Long commonPlaceId,
                String commonPlaceFloor,
                String commonPlaceRoomNumber,
                String commonPlaceName,
                LocalDate reservationDate,
                LocalTime reservationStartTime
        ) {
            this.commonPlaceId = commonPlaceId;
            this.commonPlaceFloor = commonPlaceFloor;
            this.commonPlaceRoomNumber = commonPlaceRoomNumber;
            this.commonPlaceName = commonPlaceName;
            this.reservationDate = reservationDate;
            this.reservationStartTime = reservationStartTime;
            this.reservationEndTime = reservationStartTime.plusMinutes(30);
        }
    }
}
