package com.livable.server.invitation.controller;

import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/invitation")
@RestController
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping("/places/available")
    public ResponseEntity<Success<InvitationResponse.AvailablePlacesDTO>> getAvailablePlaces() {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.getAvailablePlaces(memberId);
    }

}
