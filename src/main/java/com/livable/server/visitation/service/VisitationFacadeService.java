package com.livable.server.visitation.service;

import com.livable.server.invitation.service.InvitationService;
import com.livable.server.visitation.dto.VisitationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VisitationFacadeService {

    private final VisitationService visitationService;
    private final InvitationService invitationService;
    private final VisitorService visitorService;

    public String createQrCode(Long visitorId) {
        Long invitationId = visitorService.findInvitationId(visitorId);
        VisitationResponse.InvitationTimeDto invitationTime = invitationService.findInvitationTime(invitationId);

        return visitationService.createQrCode(invitationTime.getStartDateTime(), invitationTime.getEndDateTime());
    }

    public void validateQrCode(String qr) {
        visitationService.validateQrCode(qr);
    }
}
