package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Invitation;
import com.livable.server.entity.Visitor;
import com.livable.server.visitation.domain.VisitationErrorCode;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
}