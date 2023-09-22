package com.livable.server.reservation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Member;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.reservation.dto.AvailableReservationTimeProjection;
import com.livable.server.reservation.dto.AvailableReservationTimeProjections;
import com.livable.server.reservation.dto.ReservationResponse;
import com.livable.server.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    private final MemberRepository memberRepository;

    public List<ReservationResponse.AvailableReservationTimePerDateDto> findAvailableReservationTimes(
            Long memberId,
            Long commonPlaceId,
            LocalDate date
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalRuntimeException(MemberErrorCode.MEMBER_NOT_EXIST));

        AvailableReservationTimeProjections availableReservationTimeProjections =
                getAvailableReservationTimeProjections(member.getCompany().getId(), commonPlaceId, date);

        return availableReservationTimeProjections.toDto();
    }

    private AvailableReservationTimeProjections getAvailableReservationTimeProjections(
            Long companyId, Long commonPlaceId, LocalDate date
    ) {
        List<AvailableReservationTimeProjection> timeProjections =
                reservationRepository.findNotUsedReservationTime(companyId, commonPlaceId, date);

        return new AvailableReservationTimeProjections(timeProjections);
    }
}
