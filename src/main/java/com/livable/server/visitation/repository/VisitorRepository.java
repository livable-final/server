package com.livable.server.visitation.repository;

import com.livable.server.entity.Visitor;
import com.livable.server.visitation.dto.InvitationTimeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    @Query("select i.startDate, i.endDate, i.startTime, i.endTime" +
            " from Visitor v" +
            " join fetch Invitation i" +
            " on v.invitation" +
            " where v.id = :visitorId")
    InvitationTimeDto findByInvitationTime(@Param("visitorId") Long visitorId);
}
