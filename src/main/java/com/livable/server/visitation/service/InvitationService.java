package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Invitation;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public VisitationResponse.InvitationTimeDto findInvitationTime(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND));

        return VisitationResponse.InvitationTimeDto.from(invitation);
    }
}
