package com.livable.server.visitation.repository;

import com.livable.server.entity.Invitation;
import com.livable.server.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long>, VisitorCustomRepository {
    List<Visitor> findVisitorsByInvitation(Invitation invitation);

    @Modifying
    @Query("delete from Visitor v where v.id in :ids")
    void deleteByIdsIn(@Param("ids") List<Long> ids);

    long countByInvitation(Invitation invitation);
}
