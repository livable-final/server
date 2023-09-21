package com.livable.server.member.repository;

import com.livable.server.entity.Member;
import com.livable.server.member.dto.MyPageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT new com.livable.server.member.dto.MyPageProjection(m.name, c.name, p.balance) " +
            "FROM Member m " +
            "INNER JOIN Company c ON c.id = m.company.id " +
            "INNER JOIN Point p ON p.member.id = m.id " +
            "WHERE m.id = :memberId")
    Optional<MyPageProjection> findMemberCompanyPointData(@Param("memberId") Long memberId);
}
