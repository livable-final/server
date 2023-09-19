package com.livable.server.invitation.repository;

import com.livable.server.entity.Invitation;
import com.livable.server.invitation.dto.InvitationDetailTimeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long>, InvitationQueryRepository {

    @Query("select i.startTime as startTime, i.endTime as endTime, i.startDate as startDate, i.endDate as endDate" +
            " from Visitor v" +
            " join fetch Invitation i" +
            " on v.invitation = i" +
            " where v.id = :visitorId")
    Optional<InvitationDetailTimeDto> findInvitationDetailTimeByVisitorId(@Param("visitorId") Long visitorId);

}
