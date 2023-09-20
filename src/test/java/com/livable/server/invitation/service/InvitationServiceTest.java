package com.livable.server.invitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.entity.*;
import com.livable.server.invitation.domain.InvitationErrorCode;
import com.livable.server.invitation.dto.InvitationRequest;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.dto.InvitationResponse.AvailablePlacesDTO;
import com.livable.server.invitation.dto.InvitationResponse.CommonPlaceDTO;
import com.livable.server.invitation.repository.InvitationRepository;
import com.livable.server.invitation.repository.InvitationReservationMapRepository;
import com.livable.server.invitation.repository.OfficeRepository;
import com.livable.server.invitation.service.data.InvitationBasicData;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.reservation.repository.ReservationRepository;
import com.livable.server.visitation.repository.VisitorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private InvitationReservationMapRepository invitationReservationMapRepository;

    @InjectMocks
    private InvitationService invitationService;

    @DisplayName("[실패] 예약 가능한 리스트 목록 - 존재하지 않는 Member (400)")
    @Test
    void getAvailablePlacesFailTest_01() {
        // Given
        Long memberId = -1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.getAvailablePlaces(memberId));

        assertThat(exception.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo(InvitationErrorCode.MEMBER_NOT_EXIST.getMessage());
    }

    @DisplayName("[성공] 예약 가능한 리스트 목록 - 예약 목록이 없는 경우")
    @Test
    void getAvailablePlacesSuccess_01() {
        // Given
        InvitationBasicData basicData = InvitationBasicData.getInstance();

        given(memberRepository.findById(basicData.getMember().getId())).willReturn(Optional.of(basicData.getMember()));
        given(officeRepository.findAllByCompanyId(basicData.getCompany().getId())).willReturn(basicData.getOffices());
        given(reservationRepository.findReservationsByCompanyId(basicData.getCompany().getId())).willReturn(List.of());

        // When
        ResponseEntity<Success<AvailablePlacesDTO>> result = invitationService
                .getAvailablePlaces(basicData.getMember().getId());

        AvailablePlacesDTO data = result.getBody().getData();

        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data.getOffices().size()).isEqualTo(3);
        assertThat(data.getCommonPlaces().size()).isEqualTo(0);
    }

    @DisplayName("[성공] 예약 가능한 리스트 목록 - 예약 목록이 있는 경우")
    @Test
    void getAvailablePlacesSuccess_02() {
        // Given
        InvitationBasicData basicData = InvitationBasicData.getInstance();

        given(memberRepository.findById(basicData.getMember().getId())).willReturn(Optional.of(basicData.getMember()));
        given(officeRepository.findAllByCompanyId(basicData.getCompany().getId())).willReturn(basicData.getOffices());
        given(reservationRepository.findReservationsByCompanyId(basicData.getCompany().getId()))
                .willReturn(createReservations());

        // When
        ResponseEntity<Success<AvailablePlacesDTO>> result = invitationService
                .getAvailablePlaces(basicData.getMember().getId());

        AvailablePlacesDTO data = result.getBody().getData();

        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data.getOffices().size()).isEqualTo(3);
        assertThat(data.getCommonPlaces().size()).isEqualTo(3);

        CommonPlaceDTO combinedItem = data.getCommonPlaces().get(2);
        assertThat(combinedItem.getStartTime()).isEqualTo(LocalTime.of(10, 30, 0));
        assertThat(combinedItem.getEndTime()).isEqualTo(LocalTime.of(11, 30, 0));
    }

    @DisplayName("[실패] 초대장 생성 - 면접 초대 인원 2명")
    @Test
    void createInvitationFail_01() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .visitors(List.of(
                        InvitationRequest.VisitorCreateDTO.builder().build(),
                        InvitationRequest.VisitorCreateDTO.builder().build()
                ))
                .build();

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_INTERVIEW_MAXIMUM_NUMBER);
    }

    @DisplayName("[실패] 초대장 생성 - 존재하지 않는 Member")
    @Test
    void createInvitationFail_02() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.MEMBER_NOT_EXIST);
    }

    @DisplayName("[실패] 초대장 생성 - 종료 날짜가 시작 날짜보다 과거일 경우")
    @Test
    void createInvitationFail_03() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .startDate(LocalDateTime.of(2025, 10, 30, 0, 0, 0))
                .endDate(LocalDateTime.of(2025, 10, 29, 0, 0, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_DATE);
    }

    @DisplayName("[실패] 초대장 생성 - 종료 시간이 시작 시간과 같은 경우")
    @Test
    void createInvitationFail_04() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 0, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 10, 0, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_TIME);
    }

    @DisplayName("[실패] 초대장 생성 - 종료 시간이 시작 시간보다 과거인 경우")
    @Test
    void createInvitationFail_05() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 30, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 10, 0, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_TIME);
    }

    @DisplayName("[실패] 초대장 생성 - 입력된 시간 범위의 예상 예약 개수와 실제 예약 개수가 다른 경우")
    @Test
    void createInvitationFail_06() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .commonPlaceId(1L)
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 0, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 12, 0, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());
        given(reservationRepository.findReservationsByCommonPlaceIdAndStartDateAndEndDate(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(List.of());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_RESERVATION_COUNT);
    }

    @DisplayName("[실패] 초대장 생성 - 시작 시간 단위가 0분 또는 30분이 아닌 경우")
    @Test
    void createInvitationFail_07() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .commonPlaceId(null)
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 28, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 12, 0, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_TIME_UNIT);
    }

    @DisplayName("[실패] 초대장 생성 - 종료 시간 단위가 0분 또는 30분이 아닌 경우")
    @Test
    void createInvitationFail_08() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .commonPlaceId(null)
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 0, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 12, 14, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_TIME_UNIT);
    }

    @DisplayName("[실패] 초대장 생성 - 시작, 종료 시간 단위가 0분 또는 30분이 아닌 경우")
    @Test
    void createInvitationFail_09() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .commonPlaceId(null)
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 28, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 12, 38, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());

        // When & Then
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.createInvitation(dto, memberId));

        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_TIME_UNIT);
    }

    @DisplayName("[성공] 초대장 생성 - 예약 장소가 있는 경우")
    @Test
    void createInvitationSuccess_01() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .commonPlaceId(1L)
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 0, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 12, 0, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());
        given(reservationRepository.findReservationsByCommonPlaceIdAndStartDateAndEndDate(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(List.of(
                Reservation.builder().build(), // 2025-10-30T10:00:00
                Reservation.builder().build(), // 2025-10-30T10:30:00
                Reservation.builder().build(), // 2025-10-30T11:00:00
                Reservation.builder().build() // 2025-10-30T11:30:00
        ));
        given(invitationReservationMapRepository.save(any(InvitationReservationMap.class)))
                .willReturn(InvitationReservationMap.builder().build());

        // When & Then
        ResponseEntity<?> result = invitationService.createInvitation(dto, memberId);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("[성공] 초대장 생성 - 예약 장소가 없는 경우")
    @Test
    void createInvitationSuccess_02() {
        // Given
        Long memberId = 1L;
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .commonPlaceId(null)
                .startDate(LocalDateTime.of(2025, 10, 30, 10, 0, 0))
                .endDate(LocalDateTime.of(2025, 10, 30, 12, 0, 0))
                .visitors(List.of(InvitationRequest.VisitorCreateDTO.builder().build()))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        given(invitationRepository.save(any(Invitation.class))).willReturn(Invitation.builder().build());
        given(visitorRepository.save(any(Visitor.class))).willReturn(Visitor.builder().build());

        // When & Then
        ResponseEntity<?> result = invitationService.createInvitation(dto, memberId);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("[실패] 초대장 상세 조회 - 초대장 주인이 아닌 사람이 요청한 경우")
    @Test
    void getInvitationFail_01() {
        // Given
        Long invitationId = 1L;
        Long memberId = 1L;
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().id(memberId).build()));
        given(invitationRepository.countByIdAndMemberId(anyLong(), anyLong())).willReturn(0L);

        // When
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationService.getInvitation(invitationId, memberId));

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(InvitationErrorCode.INVALID_INVITATION_OWNER);
    }

    @DisplayName("[성공] 초대장 상세 조회")
    @Test
    void getInvitationFail_02() {
        // Given
        Long invitationId = 1L;
        Long memberId = 1L;
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().id(memberId).build()));
        given(invitationRepository.countByIdAndMemberId(anyLong(), anyLong())).willReturn(1L);
        given(invitationRepository.findInvitationAndVisitorsByInvitationId(anyLong()))
                .willReturn(any(InvitationResponse.DetailDTO.class));

        // When
        ResponseEntity<Success<InvitationResponse.DetailDTO>> result
                = invitationService.getInvitation(invitationId, memberId);

        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<InvitationResponse.ReservationDTO> createReservations() {
        return new ArrayList<>(List.of(
                new InvitationResponse.ReservationDTO(
                        1L,
                        "1",
                        "101",
                        "공용 A",
                        LocalDate.of(2023, 10, 29),
                        LocalTime.of(10, 0, 0)
                ),
                new InvitationResponse.ReservationDTO(
                        1L,
                        "1",
                        "101",
                        "공용 A",
                        LocalDate.of(2023, 10, 30),
                        LocalTime.of(10, 0, 0)
                ),
                new InvitationResponse.ReservationDTO(
                        2L,
                        "2",
                        "201",
                        "공용 B",
                        LocalDate.of(2023, 10, 30),
                        LocalTime.of(10, 30, 0)
                ),
                new InvitationResponse.ReservationDTO(
                        2L,
                        "2",
                        "201",
                        "공용 B",
                        LocalDate.of(2023, 10, 30),
                        LocalTime.of(11, 0, 0)
                )
        ));
    }

}