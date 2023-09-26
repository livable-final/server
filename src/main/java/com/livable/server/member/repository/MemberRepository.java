package com.livable.server.member.repository;

import com.livable.server.entity.Member;
import com.livable.server.home.dto.HomeResponse.BuildingInfoDto;
import com.livable.server.member.dto.AccessCardProjection;
import com.livable.server.member.dto.BuildingInfoProjection;
import com.livable.server.member.dto.MyPageProjection;
import java.util.List;
import java.util.Optional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT new com.livable.server.member.dto.MyPageProjection(m.name, c.name, p.balance) " +
            "FROM Member m " +
            "INNER JOIN Company c ON c.id = m.company.id " +
            "INNER JOIN Point p ON p.member.id = m.id " +
            "WHERE m.id = :memberId")
    Optional<MyPageProjection> findMemberCompanyPointData(@Param("memberId") Long memberId);

    @Query("SELECT distinct new com.livable.server.member.dto.AccessCardProjection(b.name, m.employeeNumber, c.name, o.floor, o.roomNumber, m.name) " +
            "FROM Member m " +
            "INNER JOIN Company c ON c.id = m.company.id " +
            "INNER JOIN Office o ON c.id = o.company.id " +
            "INNER JOIN Building b ON b.id = c.building.id " +
            "WHERE m.id = :memberId")
    List<AccessCardProjection> findAccessCardData(@Param("memberId") Long memberId);

    @Query("SELECT b.id AS buildingId, b.name AS buildingName, b.hasCafeteria AS hasCafeteria " +
            "FROM Member m " +
            "JOIN Company c " +
            "ON m.company = c " +
            "JOIN FETCH Building b " +
            "ON c.building = b " +
            "WHERE m.id = :memberId")
	  Optional<BuildingInfoDto> findBuildingInfoByMemberId(@Param("memberId") Long memberId);

}
