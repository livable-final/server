package com.livable.server.reservation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Company;
import com.livable.server.entity.InvitationReservationMap;
import com.livable.server.entity.Member;
import com.livable.server.invitation.repository.InvitationReservationMapRepository;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.reservation.dto.AvailableReservationTimeProjection;
import com.livable.server.reservation.dto.AvailableReservationTimeProjections;
import com.livable.server.reservation.dto.ReservationResponse;
import com.livable.server.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    InvitationReservationMapRepository invitationReservationMapRepository;

    @DisplayName("ReservationService.findAvailableReservationTimes 성공 테스트")
    @Test
    void findAvailableReservationTimesSuccessTest() {

        // given
        Company company = Company.builder()
                .id(1L)
                .build();

        Member member = Member.builder()
                .id(1L)
                .company(company)
                .build();

        List<AvailableReservationTimeProjection> queryResult = IntStream.range(1, 5)
                .mapToObj(idx -> new AvailableReservationTimeProjection(
                                LocalDate.now(), LocalTime.of(10, 0, 0).plusMinutes(idx * 30)
                        )
                )
                .collect(Collectors.toList());

        AvailableReservationTimeProjections projections = new AvailableReservationTimeProjections(queryResult);

        given(invitationReservationMapRepository.findAllReservationId()).willReturn(List.of(1L, 2L, 3L));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(reservationRepository.findNotUsedReservationTimeByUsedReservationIds(
                        anyLong(), anyLong(), any(LocalDate.class), any(List.class)
                )
        )
                .willReturn(queryResult);

        // when
        List<ReservationResponse.AvailableReservationTimePerDateDto> result =
                reservationService.findAvailableReservationTimes(1L, 1L, LocalDate.now());

        // then
        then(invitationReservationMapRepository).should(times(1)).findAllReservationId();
        then(memberRepository).should(times(1)).findById(anyLong());
        then(reservationRepository).should(times(1))
                .findNotUsedReservationTimeByUsedReservationIds(anyLong(), anyLong(), any(LocalDate.class), any(List.class));

        assertThat(result).usingRecursiveComparison().isEqualTo(projections.toDto());
    }

    @DisplayName("ReservationService.findAvailableReservationTimes 실패 테스트")
    @Test
    void findAvailableReservationTimesFailTest() {
        // given

        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () ->
                reservationService.findAvailableReservationTimes(1L, 1L, LocalDate.now())
        );

        // then
        then(memberRepository).should(times(1)).findById(anyLong());

        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_EXIST);
    }
}