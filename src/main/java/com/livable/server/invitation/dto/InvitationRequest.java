package com.livable.server.invitation.dto;

import com.livable.server.entity.Invitation;
import com.livable.server.entity.Member;
import com.livable.server.entity.Visitor;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvitationRequest {

    @Getter
    @Builder
    public static class CreateDTO {

        @NotNull
        private String purpose;

        private Long commonPlaceId;

        @NotNull
        private String officeName;

        private String description;

        @NotNull
        private LocalDateTime startDate;

        @NotNull
        private LocalDateTime endDate;
        private List<VisitorCreateDTO> visitors;

        public Invitation toEntity(Member member) {
            return Invitation.builder()
                    .member(member)
                    .purpose(purpose)
                    .officeName(officeName)
                    .description(description)
                    .startDate(startDate.toLocalDate())
                    .endDate(endDate.toLocalDate())
                    .startTime(startDate.toLocalTime())
                    .endTime(endDate.toLocalTime())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class VisitorCreateDTO {
        @NotNull
        private String name;

        @NotNull
        private String contact;

        public Visitor toEntity(Invitation invitation) {
            return Visitor.builder()
                    .invitation(invitation)
                    .name(name)
                    .contact(contact)
                    .firstVisitedTime(null)
                    .build();
        }
    }

}
