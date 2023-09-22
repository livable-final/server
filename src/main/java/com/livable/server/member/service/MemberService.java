package com.livable.server.member.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.dto.MemberResponse;
import com.livable.server.member.dto.MyPageProjection;
import com.livable.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse.MyPageDTO getMyPageData(Long memberId) {

        Optional<MyPageProjection> myPageProjectionOption = memberRepository.findMemberCompanyPointData(memberId);
        MyPageProjection myPageProjection = myPageProjectionOption.orElseThrow(()
                -> new GlobalRuntimeException(MemberErrorCode.MEMBER_NOT_EXIST));

        return MemberResponse.MyPageDTO.from(myPageProjection);
    }
    
    public BuildingInfoDto getBuildingInfo(Long memberId) {
		  return memberRepository.findBuildingInfoByMemberId(memberId)
			  	.orElseThrow(() -> new GlobalRuntimeException(MemberErrorCode.BUILDING_INFO_NOT_EXIST));
    }
  
}
