package com.livable.server.invitation.dto;

import com.livable.server.entity.Invitation;
import com.livable.server.entity.Member;
import com.livable.server.entity.Visitor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static com.livable.server.invitation.domain.InvitationValidationMessage.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvitationRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDTO {

        @NotNull(message = NOT_NULL)
        private String purpose;

        private Long commonPlaceId;

        @NotNull(message = NOT_NULL)
        private String officeName;

        private String description;

        @NotNull(message = NOT_NULL)
        @FutureOrPresent(message = REQUIRED_FUTURE_DATE)
        private LocalDateTime startDate;

        @NotNull(message = NOT_NULL)
        @FutureOrPresent(message = REQUIRED_FUTURE_DATE)
        private LocalDateTime endDate;

        @Valid
        @Size(min = 1, max = 30, message = REQUIRED_VISITOR_COUNT)
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisitorCreateDTO {

        @Pattern(regexp = "^[가-힣]*$", message = VISITOR_NAME_FORMAT)
        @Size(min = 2, message = VISITOR_NAME_MIN_SIZE)
        @NotNull(message = NOT_NULL)
        private String name;

        @Pattern(regexp = "^[0-9]*$", message = VISITOR_CONTACT_FORMAT)
        @Size(min = 10, message = VISITOR_CONTACT_MIN_SIZE)
        @NotNull(message = NOT_NULL)
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDTO {
        private Long commonPlaceId;
        private String description;

        @NotNull(message = NOT_NULL)
        @FutureOrPresent(message = REQUIRED_FUTURE_DATE)
        private LocalDateTime startDate;

        @NotNull(message = NOT_NULL)
        @FutureOrPresent(message = REQUIRED_FUTURE_DATE)
        private LocalDateTime endDate;

        @Valid
        @NotNull(message = NOT_NULL)
        private List<VisitorForUpdateDTO> visitors;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisitorForUpdateDTO {

        @Pattern(regexp = "^[가-힣]*$", message = VISITOR_NAME_FORMAT)
        @Size(min = 2, message = VISITOR_NAME_MIN_SIZE)
        @NotNull(message = NOT_NULL)
        private String name;

        @Pattern(regexp = "^[0-9]*$", message = VISITOR_CONTACT_FORMAT)
        @Size(min = 10, message = VISITOR_CONTACT_MIN_SIZE)
        @NotNull(message = NOT_NULL)
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
