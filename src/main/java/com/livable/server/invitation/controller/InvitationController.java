package com.livable.server.invitation.controller;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.invitation.dto.InvitationRequest;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.service.InvitationService;
import com.livable.server.member.domain.MemberErrorCode;
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
    public ResponseEntity<Success<InvitationResponse.AvailablePlacesDTO>> getAvailablePlaces(@LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        return invitationService.getAvailablePlaces(memberId);
    }

    @PostMapping
    public ResponseEntity<?> createInvitation(
            @Valid @RequestBody InvitationRequest.CreateDTO dto, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        return invitationService.createInvitation(dto, memberId);
    }

    @GetMapping
    public ResponseEntity<Success<Map<LocalDate, List<InvitationResponse.ListDTO>>>> getInvitations(@LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        return invitationService.getInvitations(memberId);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<Success<InvitationResponse.DetailDTO>> getInvitation(
            @PathVariable Long invitationId, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        return invitationService.getInvitation(invitationId, memberId);
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<?> deleteInvitation(@PathVariable Long invitationId, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        return invitationService.deleteInvitation(invitationId, memberId);
    }

    @PatchMapping("/{invitationId}")
    public ResponseEntity<?> updateInvitation(
            @PathVariable Long invitationId, @Valid @RequestBody InvitationRequest.UpdateDTO dto, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        return invitationService.updateInvitation(invitationId, dto, memberId);
    }

}
