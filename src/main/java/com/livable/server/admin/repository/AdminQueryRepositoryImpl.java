package com.livable.server.admin.repository;


import com.livable.server.admin.domain.VisitationQuery;
import com.livable.server.admin.domain.VisitationQueryCondition;
import com.livable.server.admin.dto.AdminResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.livable.server.entity.QCompany.company;
import static com.livable.server.entity.QParkingLog.parkingLog;
import static com.livable.server.entity.QVisitor.visitor;
import static com.livable.server.entity.QInvitation.invitation;
import static com.livable.server.entity.QMember.member;
import static com.livable.server.entity.QBuilding.building;

@RequiredArgsConstructor
public class AdminQueryRepositoryImpl implements AdminQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminResponse.ListDTO> findVisitationWithQuery(
            Pageable pageable, VisitationQuery visitationQuery, Long buildingId
    ) {

        BooleanBuilder dateTimeBuilder = new BooleanBuilder();

        if (visitationQuery.getStartDate() != null) {
            dateTimeBuilder.and(invitation.startDate.goe(visitationQuery.getStartDate()));
        }

        if (visitationQuery.getEndDate() != null) {
            dateTimeBuilder.and(invitation.startDate.loe(visitationQuery.getEndDate()));
        }

        BooleanBuilder queryBuilder = new BooleanBuilder();

        if (visitationQuery.getQueryCondition() != null
                && visitationQuery.getQueryCondition().equals(VisitationQueryCondition.VISITOR)
                && StringUtils.hasText(visitationQuery.getQuery())) {
            queryBuilder.and(visitor.name.contains(visitationQuery.getQuery()));
        }

        if (visitationQuery.getQueryCondition() != null
                && visitationQuery.getQueryCondition().equals(VisitationQueryCondition.COMPANY)
                && StringUtils.hasText(visitationQuery.getQuery())) {
            queryBuilder.and(company.name.contains(visitationQuery.getQuery()));
        }

        JPAQuery<AdminResponse.ProjectionForListDTO> query =
                queryFactory.select(Projections.constructor(AdminResponse.ProjectionForListDTO.class,
                                invitation.id,
                                company.name,
                                member.name,
                                visitor.id,
                                invitation.startDate,
                                invitation.startTime,
                                visitor.firstVisitedTime,
                                visitor.name,
                                invitation.officeName,
                                parkingLog.carNumber,
                                parkingLog.inTime,
                                parkingLog.outTime
                        ))
                        .from(visitor)
                        .leftJoin(parkingLog).on(parkingLog.visitor.id.eq(visitor.id))
                        .join(invitation).on(dateTimeBuilder.and(visitor.invitation.id.eq(invitation.id)))
                        .join(member).on(invitation.member.id.eq(member.id))
                        .join(company).on(member.company.id.eq(company.id))
                        .join(building).on(company.building.id.eq(building.id), building.id.eq(buildingId))
                        .where(queryBuilder)
                        .orderBy(invitation.startDate.asc());

        List<AdminResponse.ProjectionForListDTO> projection = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AdminResponse.ListDTO> content = projection.stream()
                .map(AdminResponse.ProjectionForListDTO::toListDTO)
                .collect(Collectors.toList());

        long count = query.fetchCount();


        return new PageImpl<>(content, pageable, count);
    }
}
