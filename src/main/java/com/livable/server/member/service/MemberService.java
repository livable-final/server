package com.livable.server.member.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.home.dto.HomeResponse.BuildingInfoDto;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public BuildingInfoDto getBuildingInfo(Long memberId) {
		return memberRepository.findBuildingInfoByMemberId(memberId)
				.orElseThrow(() -> new GlobalRuntimeException(MemberErrorCode.BUILDING_INFO_NOT_EXIST));
	}
}
