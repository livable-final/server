package com.livable.server.member.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.home.dto.HomeResponse;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.dto.AccessCardProjection;
import com.livable.server.member.dto.MemberResponse;
import com.livable.server.member.dto.MyPageProjection;
import com.livable.server.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public HomeResponse.AccessCardDto getAccessCardData(Long memberId) {

        Optional<AccessCardProjection> accessCardProjectionOptional = memberRepository.findAccessCardData(memberId);
        AccessCardProjection accessCardProjection = accessCardProjectionOptional.orElseThrow(()
            -> new GlobalRuntimeException(MemberErrorCode.RETRIEVE_ACCESSCARD_FAILED));

        return HomeResponse.AccessCardDto.from(accessCardProjection);
    }

    public HomeResponse.BuildingInfoDto getBuildingInfo(Long memberId) {
		  return memberRepository.findBuildingInfoByMemberId(memberId).orElseThrow(()
          -> new GlobalRuntimeException(MemberErrorCode.BUILDING_INFO_NOT_EXIST));
    }

}
