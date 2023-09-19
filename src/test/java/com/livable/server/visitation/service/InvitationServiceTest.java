package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Invitation;
import com.livable.server.invitation.dto.InvitationDetailTimeDto;
import com.livable.server.invitation.service.InvitationService;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.invitation.repository.InvitationRepository;
import com.livable.server.visitation.mock.MockInvitationDetailTimeDto;
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
        MockInvitationDetailTimeDto mockInvitationDetailTimeDto = new MockInvitationDetailTimeDto();
        Invitation invitation = Invitation.builder()
                .startTime(mockInvitationDetailTimeDto.getStartTime())
                .endTime(mockInvitationDetailTimeDto.getEndTime())
                .startDate(mockInvitationDetailTimeDto.getStartDate())
                .endDate(mockInvitationDetailTimeDto.getEndDate())
                .build();

        given(invitationRepository.findInvitationDetailTimeByVisitorId(anyLong()))
                .willReturn(Optional.of(mockInvitationDetailTimeDto));

        // When
        VisitationResponse.InvitationTimeDto invitationTime = invitationService.findInvitationTime(1L);

        // Then
        then(invitationRepository).should(times(1)).findInvitationDetailTimeByVisitorId(anyLong());
        assertThat(invitationTime).usingRecursiveComparison().isEqualTo(invitation);
    }

    @DisplayName("InvitationService.findInvitationTime 실패 테스트")
    @Test
    void findInvitationTimeFailTest() {

        // Given
        given(invitationRepository.findInvitationDetailTimeByVisitorId(anyLong())).willReturn(Optional.empty());

        // When
        GlobalRuntimeException globalRuntimeException = assertThrows(
                GlobalRuntimeException.class, () -> invitationService.findInvitationTime(anyLong())
        );

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.NOT_FOUND);
        then(invitationRepository).should(times(1)).findInvitationDetailTimeByVisitorId(anyLong());
    }
}