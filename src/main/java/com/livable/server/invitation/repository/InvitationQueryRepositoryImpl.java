package com.livable.server.invitation.repository;

import com.livable.server.entity.QVisitor;
import com.livable.server.invitation.dto.InvitationResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.livable.server.entity.QInvitation.invitation;
import static com.livable.server.entity.QVisitor.visitor;

@RequiredArgsConstructor
public class InvitationQueryRepositoryImpl implements InvitationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<InvitationResponse.ListDTO> findInvitationsByMemberId(Long memberId) {
        QVisitor subVisitorForMaxId = new QVisitor("subVisitor");
        QVisitor subVisitor = new QVisitor("subVisitor");

        return queryFactory
                .select(Projections.constructor(InvitationResponse.ListDTO.class,
                        visitor.invitation.id,
                        JPAExpressions
                                .select(subVisitor.name)
                                .from(subVisitor)
                                .where(subVisitor.id.eq(
                                        JPAExpressions
                                                .select(subVisitorForMaxId.id.min())
                                                .from(subVisitorForMaxId)
                                                .where(subVisitorForMaxId.invitation.id.eq(visitor.invitation.id))
                                )),
                        visitor.invitation.id.count(),
                        invitation.purpose,
                        invitation.officeName,
                        invitation.startDate,
                        invitation.startTime,
                        invitation.endTime
                ))
                .from(visitor)
                .innerJoin(invitation)
                .on(
                        visitor.invitation.id.eq(invitation.id),
                        invitation.member.id.eq(memberId),
                        invitation.startDate.goe(LocalDate.now())
                )
                .groupBy(visitor.invitation.id)
                .orderBy(invitation.startDate.asc(), invitation.startTime.asc())
                .fetch();
    }
}
