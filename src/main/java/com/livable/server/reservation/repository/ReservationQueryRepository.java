package com.livable.server.reservation.repository;

import com.livable.server.entity.Reservation;
import com.livable.server.invitation.dto.InvitationProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationQueryRepository {
    List<InvitationProjection.ReservationDTO> findReservationsByCompanyId(Long companyId);
    List<Reservation> findReservationsByCommonPlaceIdAndStartDateAndEndDate(
            Long commonPlaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
