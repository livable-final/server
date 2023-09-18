package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Invitation;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.repository.InvitationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @InjectMocks
    InvitationService invitationService;

    @Mock
    InvitationRepository invitationRepository;

    @DisplayName("InvitationService.findInvitationTime 성공 테스트")
    @Test
    void findInvitationTimeSuccessTest() {

        // Given
        Invitation invitation = Invitation.builder()
                .startTime(LocalTime.of(1, 1))
                .endTime(LocalTime.of(1, 1))
                .startDate(LocalDate.of(2023, 9, 18))
                .endDate(LocalDate.of(2023, 9, 18))
                .build();

        VisitationResponse.InvitationTimeDto invitationTimeDto = VisitationResponse.InvitationTimeDto.builder()
                .startTime(LocalTime.of(1, 1))
                .endTime(LocalTime.of(1, 1))
                .startDate(LocalDate.of(2023, 9, 18))
                .endDate(LocalDate.of(2023, 9, 18))
                .build();

        given(invitationRepository.findById(anyLong())).willReturn(Optional.of(invitation));

        // When
        VisitationResponse.InvitationTimeDto invitationTime = invitationService.findInvitationTime(anyLong());

        // Then
        then(invitationRepository).should(times(1)).findById(anyLong());
        assertThat(invitationTime).usingRecursiveComparison().isEqualTo(invitationTimeDto);
    }

    @DisplayName("InvitationService.findInvitationTime 실패 테스트")
    @Test
    void findInvitationTimeFailTest() {

        // Given
        Invitation invitation = Invitation.builder()
                .startTime(LocalTime.of(1, 1))
                .endTime(LocalTime.of(1, 1))
                .startDate(LocalDate.of(2023, 9, 18))
                .endDate(LocalDate.of(2023, 9, 18))
                .build();

        VisitationResponse.InvitationTimeDto invitationTimeDto = VisitationResponse.InvitationTimeDto.builder()
                .startTime(LocalTime.of(1, 1))
                .endTime(LocalTime.of(1, 1))
                .startDate(LocalDate.of(2023, 9, 18))
                .endDate(LocalDate.of(2023, 9, 18))
                .build();

        given(invitationRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () -> invitationService.findInvitationTime(anyLong()));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.NOT_FOUND);
        then(invitationRepository).should(times(1)).findById(anyLong());
    }
}