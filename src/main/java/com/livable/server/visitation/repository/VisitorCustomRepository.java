package com.livable.server.visitation.repository;

import com.livable.server.visitation.dto.VisitationResponse;

public interface VisitorCustomRepository {

    VisitationResponse.DetailInformationDto findVisitationDetailInformationById(Long visitorId);
}
