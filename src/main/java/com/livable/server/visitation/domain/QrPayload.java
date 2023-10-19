package com.livable.server.visitation.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QrPayload {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
