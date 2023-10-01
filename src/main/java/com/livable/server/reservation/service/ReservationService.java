package com.livable.server.reservation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Member;
import com.livable.server.invitation.repository.InvitationReservationMapRepository;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.reservation.dto.AvailableReservationTimeProjection;
import com.livable.server.reservation.dto.AvailableReservationTimeProjections;
import com.livable.server.reservation.dto.ReservationResponse;
import com.livable.server.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final InvitationReservationMapRepository invitationReservationMapRepository;

    public List<ReservationResponse.AvailableReservationTimePerDateDto> findAvailableReservationTimes(
            Long memberId,
            Long commonPlaceId,
            LocalDate startDate,
            LocalDate endDate
    ) {


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalRuntimeException(MemberErrorCode.MEMBER_NOT_EXIST));

        AvailableReservationTimeProjections availableReservationTimeProjections =
                getAvailableReservationTimeProjections(member.getCompany().getId(), commonPlaceId, startDate, endDate);

        return availableReservationTimeProjections.toDto();
    }

    private AvailableReservationTimeProjections getAvailableReservationTimeProjections(
            Long companyId, Long commonPlaceId, LocalDate startDate, LocalDate endDate
    ) {
        List<Long> usedReservationIds = invitationReservationMapRepository.findAllReservationId();
        List<AvailableReservationTimeProjection> timeProjections =
                reservationRepository.findNotUsedReservationTimeByUsedReservationIds(
                        companyId, commonPlaceId, startDate, endDate, usedReservationIds
                );

        return new AvailableReservationTimeProjections(timeProjections);
    }
}
