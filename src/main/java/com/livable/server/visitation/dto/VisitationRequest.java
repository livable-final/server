package com.livable.server.visitation.dto;

import com.livable.server.visitation.domain.VisitationValidationMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitationRequest {

    @Getter
    public static class ValidateQrCodeDto {

        @NotNull
        private String qr;
    }

    @Getter
    public static class RegisterParkingDto {

        @NotNull
        @NotEmpty
        @Pattern(regexp = "^\\d{2,3}[가-힣]{1}\\d{4}$", message = VisitationValidationMessage.INVALID_CAR_NUMBER)
        private String carNumber;
    }
}
