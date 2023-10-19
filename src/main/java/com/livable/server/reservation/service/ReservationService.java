package com.livable.server.reservation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Member;
import com.livable.server.invitation.repository.InvitationReservationMapRepository;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.reservation.domain.ReservationRequest;
import com.livable.server.reservation.dto.AvailableReservationTimeProjection;
import com.livable.server.reservation.dto.AvailableReservationTimeProjections;
import com.livable.server.reservation.dto.ReservationResponse;
import com.livable.server.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final InvitationReservationMapRepository invitationReservationMapRepository;

    public List<ReservationResponse.AvailableReservationTimePerDateDto> findAvailableReservationTimes(
            Long memberId,
            Long commonPlaceId,
            ReservationRequest.DateQuery dateQuery
    ) {


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalRuntimeException(MemberErrorCode.MEMBER_NOT_EXIST));

        AvailableReservationTimeProjections availableReservationTimeProjections =
                getAvailableReservationTimeProjections(member.getCompany().getId(), commonPlaceId, dateQuery);

        return availableReservationTimeProjections.toDto();
    }

    private AvailableReservationTimeProjections getAvailableReservationTimeProjections(
            Long companyId, Long commonPlaceId, ReservationRequest.DateQuery dateQuery
    ) {
        List<Long> usedReservationIds = invitationReservationMapRepository.findAllReservationId();
        List<AvailableReservationTimeProjection> timeProjections =
                reservationRepository.findNotUsedReservationTimeByUsedReservationIds(
                        companyId, commonPlaceId, dateQuery.getStartDate(), dateQuery.getEndDate(), usedReservationIds
                );

        return new AvailableReservationTimeProjections(timeProjections);
    }
}
