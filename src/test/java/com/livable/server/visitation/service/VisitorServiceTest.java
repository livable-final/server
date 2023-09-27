package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Invitation;
import com.livable.server.entity.Visitor;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.mock.MockDetailInformationDto;
import com.livable.server.visitation.repository.VisitorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @InjectMocks
    VisitorService visitorService;

    @Mock
    VisitorRepository visitorRepository;

    @DisplayName("VisitorService.findInvitationId 성공 테스트")
    @Test
    void findInvitationIdSuccessTest() {
        // Given
        Invitation invitation = Invitation.builder()
                .id(1L)
                .build();

        Visitor visitor = Visitor.builder()
                .name("태윤초이")
                .invitation(invitation)
                .build();

        given(visitorRepository.findById(anyLong())).willReturn(Optional.of(visitor));

        // When
        Long invitationId = visitorService.findInvitationId(1L);

        // Then
        assertThat(invitationId).isEqualTo(1L);
        then(visitorRepository).should(times(1)).findById(anyLong());
    }

    @DisplayName("VisitorService.findInvitationId 실패 테스트")
    @Test
    void findInvitationIdFailTest() {
        // Given
        Invitation invitation = Invitation.builder()
                .id(1L)
                .build();

        Visitor visitor = Visitor.builder()
                .name("태윤초이")
                .invitation(invitation)
                .build();

        given(visitorRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () -> visitorService.findInvitationId(1L));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.NOT_FOUND);
        then(visitorRepository).should(times(1)).findById(anyLong());
    }

    @DisplayName("VisitorService.findVisitationDetailInformation 성공 테스트")
    @Test
    void findVisitationDetailInformationByIdSuccessTest() {
        // Given
        MockDetailInformationDto mockDetailInformationDto = new MockDetailInformationDto();
        given(visitorRepository.findVisitationDetailInformationById(anyLong())).willReturn(Optional.of(mockDetailInformationDto));

        // When
        VisitationResponse.DetailInformationDto detailInformationDto = visitorService.findVisitationDetailInformation(1L);

        // Then
        assertThat(detailInformationDto).isEqualTo(mockDetailInformationDto);
        then(visitorRepository).should(times(1)).findVisitationDetailInformationById(anyLong());
    }

    @DisplayName("VisitorService.findVisitationDetailInformation 실패 테스트")
    @Test
    void findVisitationDetailInformationByIdFailTest() {
        // Given
        given(visitorRepository.findVisitationDetailInformationById(anyLong())).willReturn(Optional.empty());

        // When
        GlobalRuntimeException globalRuntimeException =
                assertThrows(
                        GlobalRuntimeException.class, () -> visitorService.findVisitationDetailInformation(1L)
                );

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.NOT_FOUND);
        then(visitorRepository).should(times(1)).findVisitationDetailInformationById(anyLong());
    }

    @DisplayName("VisitorService.doEntrance 성공 테스트")
    @Test
    void doEntranceSuccessTest() {
        // given
        Visitor visitor = spy(Visitor.class);
        given(visitorRepository.findById(anyLong())).willReturn(Optional.of(visitor));
        willDoNothing().given(visitor).entrance();

        // when
        visitorService.doEntrance(1L);

        // then
        then(visitorRepository).should(times(1)).findById(anyLong());
        then(visitor).should(times(1)).entrance();
    }

    @DisplayName("VisitorService.doEntrance 실패 테스트")
    @Test
    void doEntranceFailTest() {
        // given
        given(visitorRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        GlobalRuntimeException globalRuntimeException =
                assertThrows(GlobalRuntimeException.class, () -> visitorService.doEntrance(1L));

        // then
        then(visitorRepository).should(times(1)).findById(anyLong());
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.NOT_FOUND);
    }
}