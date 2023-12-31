package com.livable.server.invitation.repository;

import com.livable.server.entity.InvitationReservationMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvitationReservationMapRepository extends JpaRepository<InvitationReservationMap, Long> {

    @Modifying
    @Query("delete from InvitationReservationMap irm where irm.invitation.id = :invitationId")
    void deleteAllByInvitationId(Long invitationId);

    @Query("select ir.reservation.id from InvitationReservationMap ir")
    List<Long> findAllReservationId();
}
