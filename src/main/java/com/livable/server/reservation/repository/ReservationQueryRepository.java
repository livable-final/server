package com.livable.server.reservation.repository;

import com.livable.server.entity.Reservation;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.reservation.dto.AvailableReservationTimeProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationQueryRepository {
    List<InvitationResponse.ReservationDTO> findReservationsByCompanyId(Long companyId);
    List<Reservation> findReservationsByCommonPlaceIdAndStartDateAndEndDate(
            Long commonPlaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<AvailableReservationTimeProjection> findNotUsedReservationTime(
            Long companyId, Long commonPlaceId, LocalDate date
    );

    List<AvailableReservationTimeProjection> findNotUsedReservationTimeByUsedReservationIds(
            Long companyId, Long commonPlaceId, LocalDate date, List<Long> reservationIds
    );
}
