package com.livable.server.reservation.repository;

import com.livable.server.invitation.dto.InvitationProjection;

import java.util.List;

public interface ReservationQueryRepository {
    List<InvitationProjection.ReservationDTO> findReservationsByCompanyId(Long companyId);
}
