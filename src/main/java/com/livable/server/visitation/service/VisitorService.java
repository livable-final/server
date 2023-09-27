package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Visitor;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public void doEntrance(final Long visitorId) {

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND));

        if (visitor.getFirstVisitedTime() == null) {
            visitor.updateFirstVisitedTime();
        }
    }

    public Long findInvitationId(final Long visitorId) {
        return visitorRepository.findById(visitorId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND))
                .getInvitation()
                .getId();
    }

    public Visitor findById(final Long visitorId) {
        return visitorRepository.findById(visitorId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND));
    }

    public VisitationResponse.DetailInformationDto findVisitationDetailInformation(final Long visitorId) {
        return visitorRepository.findVisitationDetailInformationById(visitorId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND));
    }
}
