package com.livable.server.invitation.controller;

import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.invitation.dto.InvitationRequest;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Success<Map<LocalDate, List<InvitationResponse.ListDTO>>>> getInvitations() {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.getInvitations(memberId);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<Success<InvitationResponse.DetailDTO>> getInvitation(@PathVariable Long invitationId) {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.getInvitation(invitationId, memberId);
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<?> deleteInvitation(@PathVariable Long invitationId) {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.deleteInvitation(invitationId, memberId);
    }

    @PatchMapping("/{invitationId}")
    public ResponseEntity<?> updateInvitation(
            @PathVariable Long invitationId, @Valid @RequestBody InvitationRequest.UpdateDTO dto) {

        Long memberId = 1L; // TODO: JWT 토큰에서 추출한 값으로 수정

        return invitationService.updateInvitation(invitationId, dto, memberId);
    }

}
