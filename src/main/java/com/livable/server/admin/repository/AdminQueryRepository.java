package com.livable.server.admin.repository;

import com.livable.server.admin.domain.VisitationQuery;
import com.livable.server.admin.dto.AdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminQueryRepository {

    Page<AdminResponse.ListDTO> findVisitationWithQuery(
            Pageable pageable, VisitationQuery visitationQuery, Long buildingId
    );
}
