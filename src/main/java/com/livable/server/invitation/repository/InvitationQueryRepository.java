package com.livable.server.invitation.repository;

import com.livable.server.invitation.dto.InvitationResponse;

import java.util.List;

public interface InvitationQueryRepository {
    List<InvitationResponse.ListDTO> findInvitationsByMemberId(Long memberId);
}
