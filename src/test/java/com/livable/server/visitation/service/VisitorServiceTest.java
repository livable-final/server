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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
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

    @DisplayName("VisitorService.updateFirstEntranceTime 성공 테스트")
    @Test
    void updateFirstEntranceTimeSuccessTest_1() {
        // given
        Visitor visitor = mock(Visitor.class);
        given(visitorRepository.findById(anyLong())).willReturn(Optional.of(visitor));
        given(visitor.getFirstVisitedTime()).willReturn(null);
        willDoNothing().given(visitor).updateFirstVisitedTime();

        // when
        visitorService.updateFirstEntranceTime(1L);

        // then
        then(visitorRepository).should(times(1)).findById(anyLong());
        then(visitor).should(times(1)).getFirstVisitedTime();
        then(visitor).should(times(1)).updateFirstVisitedTime();
    }

    @DisplayName("VisitorService.updateFirstEntranceTime 성공 테스트")
    @Test
    void updateFirstEntranceTimeSuccessTest_2() {
        // given
        Visitor visitor = mock(Visitor.class);
        given(visitorRepository.findById(anyLong())).willReturn(Optional.of(visitor));
        given(visitor.getFirstVisitedTime()).willReturn(LocalDateTime.now());

        // when
        visitorService.updateFirstEntranceTime(1L);

        // then
        then(visitorRepository).should(times(1)).findById(anyLong());
        then(visitor).should(times(1)).getFirstVisitedTime();
        then(visitor).should(times(0)).updateFirstVisitedTime();
    }

    @DisplayName("VisitorService.updateFirstEntranceTime 실패 테스트")
    @Test
    void updateFirstEntranceTimeFailTest() {
        // given
        given(visitorRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        GlobalRuntimeException globalRuntimeException =
                assertThrows(
                        GlobalRuntimeException.class, () -> visitorService.updateFirstEntranceTime(1L)
                );
        // then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.NOT_FOUND);
        then(visitorRepository).should(times(1)).findById(anyLong());
    }
}