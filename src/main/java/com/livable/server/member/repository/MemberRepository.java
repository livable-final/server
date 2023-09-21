package com.livable.server.member.repository;

import com.livable.server.entity.Member;
import com.livable.server.home.dto.HomeResponse.BuildingInfoDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query(value = "select b.id as buildingId, b.name as buildingName, b.hasCafeteria as hasCafeteria" +
			" from Member m " +
			" join Company c" +
			" on m.company = c" +
			" join fetch Building b " +
			" on c.building = b" +
			" where m.id = :memberId")
	Optional<BuildingInfoDto> findBuildingInfoByMemberId(@Param("memberId") Long memberId);
}
