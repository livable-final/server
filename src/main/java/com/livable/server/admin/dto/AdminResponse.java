package com.livable.server.admin.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminResponse {

    @Getter
    @Builder
    public static class ListDTO {
        private Long invitationId;
        private String company;
        private String host;
        private Long visitorId;
        private LocalDateTime startDateTime;
        private LocalDateTime visitTime;
        private String visitorName;
        private String officeName;
        private String carNumber;
        private LocalDateTime inTime;
        private LocalDateTime outTime;
        private Integer stayTime;
    }

    @Getter
    @AllArgsConstructor
    public static class ProjectionForListDTO {
        private Long invitationId;
        private String company;
        private String host;
        private Long visitorId;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDateTime visitTime;
        private String visitorName;
        private String officeName;
        private String carNumber;
        private LocalDateTime inTime;
        private LocalDateTime outTime;

        public ListDTO toListDTO() {
            return ListDTO.builder()
                    .invitationId(invitationId)
                    .company(company)
                    .host(host)
                    .visitorId(visitorId)
                    .startDateTime(LocalDateTime.of(startDate, startTime))
                    .visitTime(visitTime)
                    .visitorName(visitorName)
                    .officeName(officeName)
                    .carNumber(carNumber)
                    .inTime(inTime)
                    .outTime(outTime)
                    .stayTime(calculateStayTime(inTime, outTime))
                    .build();
        }

        private Integer calculateStayTime(LocalDateTime inTime, LocalDateTime outTime) {
            if (inTime != null && outTime != null) {
                Duration duration = Duration.between(inTime, outTime);

                return Long.valueOf(duration.getSeconds()).intValue() / 60;
            }

            return null;
        }
    }
}
