package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.ParkingLog;
import com.livable.server.entity.Visitor;
import com.livable.server.invitation.service.InvitationService;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VisitationFacadeService {

    private final VisitationService visitationService;
    private final InvitationService invitationService;
    private final VisitorService visitorService;
    private final ParkingLogService parkingLogService;

    public String createQrCode(Long visitorId) {
        Long invitationId = visitorService.findInvitationId(visitorId);
        VisitationResponse.InvitationTimeDto invitationTime = invitationService.findInvitationTime(invitationId);

        return visitationService.createQrCode(invitationTime.getStartDateTime(), invitationTime.getEndDateTime());
    }

    public void validateQrCode(String qr) {
        visitationService.validateQrCode(qr);
    }

    @Transactional
    public void registerParking(Long visitorId, String carNumber) {
        validateDuplicationRegister(visitorId);
        Visitor visitor = visitorService.findById(visitorId);
        parkingLogService.registerParkingLog(visitor, carNumber);
    }

    private void validateDuplicationRegister(Long visitorId) {
        if (parkingLogService.findParkingLogByVisitorId(visitorId).isPresent()) {
            throw new GlobalRuntimeException(VisitationErrorCode.ALREADY_REGISTER_PARKING);
        }
    }
}
