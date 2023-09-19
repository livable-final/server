package com.livable.server.invitation.repository;

import com.livable.server.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long>, InvitationQueryRepository {
}
