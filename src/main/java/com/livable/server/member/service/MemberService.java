package com.livable.server.member.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.home.dto.HomeResponse;
import com.livable.server.home.dto.HomeResponse.AccessCardDto;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.dto.AccessCardProjection;
import com.livable.server.member.dto.BuildingInfoProjection;
import com.livable.server.member.dto.MemberResponse;
import com.livable.server.member.dto.MyPageProjection;
import com.livable.server.member.repository.MemberRepository;
import java.util.List;
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

        List<AccessCardProjection> accessCardProjectionOptional = memberRepository.findAccessCardData(memberId);

        if (accessCardProjectionOptional.isEmpty()) {
            throw new GlobalRuntimeException(MemberErrorCode.RETRIEVE_ACCESSCARD_FAILED);
        }

        return AccessCardDto.from(accessCardProjectionOptional.get(0));
    }

    public HomeResponse.BuildingInfoDto getBuildingInfo(Long memberId) {
        Optional<BuildingInfoProjection> buildingInfoProjectionOptional = memberRepository.findBuildingInfoByMemberId(memberId);
        BuildingInfoProjection buildingInfoProjection = buildingInfoProjectionOptional.orElseThrow(()
            -> new GlobalRuntimeException(MemberErrorCode.BUILDING_INFO_NOT_EXIST));

        return HomeResponse.BuildingInfoDto.from(buildingInfoProjection);
    }
  
}
