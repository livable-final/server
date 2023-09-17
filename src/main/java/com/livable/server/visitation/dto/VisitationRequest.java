package com.livable.server.visitation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitationRequest {

    @Getter
    public static class ValidateQrDto {
        private String qr;
    }
}
