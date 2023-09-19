package com.livable.server.visitation.dto;

import com.beust.ah.A;
import com.livable.server.entity.Invitation;
import com.livable.server.invitation.dto.InvitationDetailTimeDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitationResponse {

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

        public static InvitationTimeDto from(InvitationDetailTimeDto invitationDetailTimeDto) {
            return InvitationTimeDto.builder()
                    .startTime(invitationDetailTimeDto.getStartTime())
                    .endTime(invitationDetailTimeDto.getEndTime())
                    .startDate(invitationDetailTimeDto.getStartDate())
                    .endDate(invitationDetailTimeDto.getEndDate())
                    .build();
        }
    }
}
