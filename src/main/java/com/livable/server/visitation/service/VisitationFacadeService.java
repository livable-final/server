package com.livable.server.visitation.service;

import com.livable.server.visitation.dto.VisitationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VisitationFacadeService {

    private final VisitationService visitationService;
    private final InvitationService invitationService;
    private final VisitorService visitorService;

    public String createQrCode(LocalDateTime startDate, LocalDateTime endDate) {
        return visitationService.createQrCode(startDate, endDate);
    }

    public void validateQrCode(String qr) {
        visitationService.validateQrCode(qr);
    }

    public VisitationResponse.InvitationTimeDto findInvitationTime(Long visitorId) {
        Long invitationId = visitorService.findInvitationId(visitorId);
        return invitationService.findInvitationTime(invitationId);
    }
}
