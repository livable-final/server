package com.livable.server.reservation.repository;

import com.livable.server.entity.Reservation;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.reservation.dto.AvailableReservationTimeProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.livable.server.entity.QCommonPlace.commonPlace;
import static com.livable.server.entity.QInvitationReservationMap.invitationReservationMap;
import static com.livable.server.entity.QReservation.reservation;

@RequiredArgsConstructor
@Repository
public class ReservationQueryRepositoryImpl implements ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<InvitationResponse.ReservationDTO> findReservationsByCompanyId(Long companyId) {
        return queryFactory
                .selectDistinct(Projections.constructor(InvitationResponse.ReservationDTO.class,
                        commonPlace.id,
                        commonPlace.floor,
                        commonPlace.roomNumber,
                        commonPlace.name
                ))
                .from(reservation)
                .innerJoin(reservation.commonPlace, commonPlace)
                .where(
                        reservation.company.id.eq(companyId),
                        reservation.id.notIn(
                                JPAExpressions
                                        .select(invitationReservationMap.reservation.id)
                                        .from(invitationReservationMap)
                                        .where(invitationReservationMap.reservation.id.eq(reservation.id))
                        )
                )
                .orderBy(reservation.commonPlace.id.asc())
                .fetch();
    }

    @Override
    public List<Reservation> findReservationsByCommonPlaceIdAndStartDateAndEndDate(
            Long commonPlaceId, LocalDateTime startDateTime, LocalDateTime endDateTime
    ) {

        return queryFactory
                .selectFrom(reservation)
                .where(
                        reservation.commonPlace.id.eq(commonPlaceId),
                        reservation.date.between(
                                startDateTime.toLocalDate(),
                                endDateTime.toLocalDate()
                        ),
                        reservation.time.between(
                                startDateTime.toLocalTime(),
                                endDateTime.toLocalTime().minusMinutes(30)
                        ),
                        reservation.id.notIn(
                                JPAExpressions
                                        .select(invitationReservationMap.reservation.id)
                                        .from(invitationReservationMap)
                                        .where(invitationReservationMap.reservation.id.eq(reservation.id))
                        )
                )
                .orderBy(reservation.date.asc(), reservation.time.asc())
                .fetch();
    }

    @Override
    public List<AvailableReservationTimeProjection> findNotUsedReservationTime(
            Long companyId, Long commonPlaceId, LocalDate date
    ) {
        return queryFactory
                .select(Projections.constructor(AvailableReservationTimeProjection.class,
                                reservation.date,
                                reservation.time
                        )
                )
                .from(reservation)
                .where(reservation.id.notIn(
                                JPAExpressions.select(invitationReservationMap.reservation.id)
                                        .from(invitationReservationMap)
                        ),
                        reservation.company.id.eq(companyId),
                        reservation.date.eq(date),
                        reservation.commonPlace.id.eq(commonPlaceId)
                )
                .fetch();
    }

    @Override
    public List<AvailableReservationTimeProjection> findNotUsedReservationTimeByUsedReservationIds(
            Long companyId, Long commonPlaceId, LocalDate startDate, LocalDate endDate, List<Long> reservationIds
    ) {
        return queryFactory
                .select(Projections.constructor(AvailableReservationTimeProjection.class,
                                reservation.date,
                                reservation.time
                        )
                )
                .from(reservation)
                .where(reservation.id.notIn(
                                reservationIds
                        ),
                        reservation.company.id.eq(companyId),
                        reservation.date.goe(startDate).and(reservation.date.loe(endDate)),
                        reservation.commonPlace.id.eq(commonPlaceId)
                )
                .fetch();
    }
}
