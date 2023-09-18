package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.InvitationTimeDto;
import com.livable.server.visitation.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public Long findInvitationId(Long visitorId) {
        return visitorRepository.findById(visitorId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND))
                .getInvitation()
                .getId();
    }

    public InvitationTimeDto findVisitationTime(Long visitorId) {
        return visitorRepository.findByInvitationTime(visitorId);
    }
}
