package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Visitor;
import com.livable.server.invitation.service.InvitationService;
import com.livable.server.visitation.domain.VisitationErrorCode;
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
    private final ParkingLogService parkingLogService;

    public VisitationResponse.DetailInformationDto findVisitationDetailInformation(Long visitorId) {
        return visitorService.findVisitationDetailInformation(visitorId);
    }

    public String createQrCode(final Long visitorId) {
        VisitationResponse.InvitationTimeDto invitationTime = invitationService.findInvitationTime(visitorId);

        return visitationService.createQrCode(invitationTime.getStartDateTime(), invitationTime.getEndDateTime());
    }

    public void validateQrCode(final String qr) {
        visitationService.validateQrCode(qr);
    }

    @Transactional
    public void registerParking(final Long visitorId, final String carNumber) {
        validateDuplicationRegister(visitorId);
        Visitor visitor = visitorService.findById(visitorId);
        parkingLogService.registerParkingLog(visitor, carNumber);
    }

    private void validateDuplicationRegister(final Long visitorId) {
        if (parkingLogService.findParkingLogByVisitorId(visitorId).isPresent()) {
            throw new GlobalRuntimeException(VisitationErrorCode.ALREADY_REGISTER_PARKING);
        }
    }
}
