package com.livable.server.invitation.repository;

import com.livable.server.invitation.dto.InvitationProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.livable.server.entity.QCommonPlace.commonPlace;
import static com.livable.server.entity.QInvitationReservationMap.invitationReservationMap;
import static com.livable.server.entity.QReservation.reservation;

@RequiredArgsConstructor
@Repository
public class ReservationQueryRepositoryImpl implements ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<InvitationProjection.ReservationDTO> findReservationsByCompanyId(Long companyId) {
        return queryFactory
                .select(Projections.constructor(InvitationProjection.ReservationDTO.class,
                        commonPlace.id,
                        commonPlace.floor,
                        commonPlace.roomNumber,
                        commonPlace.name,
                        reservation.date,
                        reservation.time
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
                .orderBy(reservation.commonPlace.id.asc(), reservation.date.asc(), reservation.time.asc())
                .fetch();
    }
}
