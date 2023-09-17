package com.livable.server.invitation.repository;

import com.livable.server.invitation.dto.InvitationProjection;

import java.util.List;

public interface ReservationQueryRepository {
    List<InvitationProjection.ReservationDTO> findReservationsByCompanyId(Long companyId);
}
