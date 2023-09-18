package com.livable.server.invitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.invitation.domain.InvitationErrorCode;
import com.livable.server.invitation.dto.InvitationResponse.AvailablePlacesDTO;
import com.livable.server.invitation.dto.InvitationResponse.CommonPlaceDTO;
import com.livable.server.invitation.repository.MemberRepository;
import com.livable.server.invitation.repository.OfficeRepository;
import com.livable.server.invitation.repository.ReservationRepository;
import com.livable.server.invitation.service.data.InvitationBasicData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.livable.server.invitation.dto.InvitationProjection.ReservationDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private ReservationRepository reservationRepository;

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

    private List<ReservationDTO> createReservations() {
        return new ArrayList<>(List.of(
                new ReservationDTO(
                        1L,
                        "1",
                        "101",
                        "공용 A",
                        LocalDate.of(2023, 10, 29),
                        LocalTime.of(10, 0, 0)
                ),
                new ReservationDTO(
                        1L,
                        "1",
                        "101",
                        "공용 A",
                        LocalDate.of(2023, 10, 30),
                        LocalTime.of(10, 0, 0)
                ),
                new ReservationDTO(
                        2L,
                        "2",
                        "201",
                        "공용 B",
                        LocalDate.of(2023, 10, 30),
                        LocalTime.of(10, 30, 0)
                ),
                new ReservationDTO(
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