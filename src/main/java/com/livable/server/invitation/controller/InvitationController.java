package com.livable.server.invitation.controller;

import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.invitation.dto.InvitationRequest;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PostMapping
    public ResponseEntity<?> createInvitation(
            @Valid @RequestBody InvitationRequest.CreateDTO dto) {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.createInvitation(dto, memberId);
    }

    @GetMapping
    public ResponseEntity<Success<List<InvitationResponse.ListDTO>>> getInvitations() {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.getInvitations(memberId);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<Success<InvitationResponse.DetailDTO>> getInvitation(@PathVariable Long invitationId) {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.getInvitation(invitationId, memberId);
    }

}
