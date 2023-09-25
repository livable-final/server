package com.livable.server.member.repository;

import com.livable.server.entity.Member;
import com.livable.server.member.dto.BuildingInfoProjection;
import com.livable.server.member.dto.MyPageProjection;
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

    @Query("SELECT new com.livable.server.member.dto.BuildingInfoProjection (b.id, b.name, b.hasCafeteria) " +
            "FROM Member m " +
            "JOIN Company c " +
            "ON m.company = c " +
            "JOIN Building b " +
            "ON c.building = b " +
            "WHERE m.id = :memberId")
	  Optional<BuildingInfoProjection> findBuildingInfoByMemberId(@Param("memberId") Long memberId);
  
}
