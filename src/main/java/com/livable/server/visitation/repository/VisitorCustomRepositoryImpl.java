package com.livable.server.visitation.repository;

import com.livable.server.entity.*;
import com.livable.server.visitation.dto.VisitationResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class VisitorCustomRepositoryImpl implements VisitorCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<VisitationResponse.DetailInformationDto> findVisitationDetailInformationById(final Long visitorId) {
        final QInvitation invitation = QInvitation.invitation;
        final QBuilding building = QBuilding.building;
        final QCompany company = QCompany.company;
        final QMember member = QMember.member;
        final QVisitor visitor = QVisitor.visitor;

        JPAQuery<VisitationResponse.DetailInformationDto> query = queryFactory
                .select(Projections.constructor(VisitationResponse.DetailInformationDto.class,
                        invitation.startDate,
                        invitation.startTime,
                        invitation.endDate,
                        invitation.endTime,
                        building.name,
                        invitation.officeName,
                        building.representativeImageUrl,
                        building.name,
                        building.address,
                        building.parkingCostInformation,
                        building.scale,
                        invitation.description,
                        member.name,
                        company.name,
                        member.contact,
                        member.businessCardImageUrl
                ))
                .from(visitor)
                .innerJoin(invitation).on(visitor.invitation.id.eq(invitation.id))
                .innerJoin(member).on(invitation.member.id.eq(member.id))
                .innerJoin(company).on(member.company.id.eq(company.id))
                .innerJoin(building).on(company.building.id.eq(building.id))
                .where(visitor.id.eq(visitorId));

        return Optional.ofNullable(query.fetchJoin().fetchOne());
    }

    @Override
    public Optional<Long> findBuildingIdById(Long visitorId) {

        final QBuilding building = QBuilding.building;
        final QVisitor visitor = QVisitor.visitor;
        final QInvitation invitation = QInvitation.invitation;
        final QMember member = QMember.member;
        final QCompany company = QCompany.company;

        JPAQuery<Long> query = queryFactory
                .select(building.id)
                .from(visitor)
                .innerJoin(invitation).on(visitor.invitation.id.eq(invitation.id))
                .innerJoin(member).on(invitation.member.id.eq(member.id))
                .innerJoin(company).on(member.company.id.eq(company.id))
                .innerJoin(building).on(company.building.id.eq(building.id))
                .where(visitor.id.eq(visitorId));

        return Optional.ofNullable(query.fetchJoin().fetchOne());
    }
}
