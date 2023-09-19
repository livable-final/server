package com.livable.server.visitation.dto;

import com.livable.server.invitation.domain.InvitationValidationGuideMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitationRequest {

    @Getter
    public static class ValidateQrDto {
        private String qr;
    }

    @Getter
    public static class RegisterParkingDto {
        @Pattern(regexp = "^\\d{2,3}[가-힣]{1}\\d{4}$", message = InvitationValidationGuideMessage.INVALID_CAR_NUMBER)
        private String carNumber;
    }
}
