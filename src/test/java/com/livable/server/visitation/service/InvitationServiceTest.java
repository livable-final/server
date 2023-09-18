package com.livable.server.visitation.service;

import com.livable.server.entity.Invitation;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.repository.InvitationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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
        assertThat(invitationTime).usingRecursiveComparison().isEqualTo(invitationTimeDto);
    }

}