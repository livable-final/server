package com.livable.server.reservation.repository;

import com.livable.server.entity.Reservation;
import com.livable.server.invitation.dto.InvitationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationQueryRepository {
    List<InvitationResponse.ReservationDTO> findReservationsByCompanyId(Long companyId);
    List<Reservation> findReservationsByCommonPlaceIdAndStartDateAndEndDate(
            Long commonPlaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
