package com.livable.server.visitation.repository;

import com.livable.server.visitation.dto.VisitationResponse;

import java.util.Optional;

public interface VisitorCustomRepository {

    Optional<VisitationResponse.DetailInformationDto> findVisitationDetailInformationById(final Long visitorId);
}
