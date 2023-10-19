package com.livable.server.member.service;

import static com.livable.server.home.dto.HomeResponse.BuildingInfoDto;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.home.dto.HomeResponse.AccessCardDto;
import com.livable.server.member.dto.AccessCardProjection;
import com.livable.server.member.dto.BuildingInfoProjection;
import com.livable.server.member.dto.MemberResponse;
import com.livable.server.member.dto.MyPageProjection;
import com.livable.server.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Nested
    @DisplayName("마이페이지 서비스  단위 테스트")
    class MyRestaurantReview {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Long memberId = 1L;
            String memberName = "TestName";
            String companyName = "TestCompany";
            Integer pointValance = 200;

            MyPageProjection mockResult
                    = new MyPageProjection(memberName, companyName, pointValance);

            Mockito.when(memberRepository.findMemberCompanyPointData(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(mockResult));

            // When
            MemberResponse.MyPageDTO actual = memberService.getMyPageData(memberId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(memberName, actual.getMemberName()),
                    () -> Assertions.assertEquals(companyName, actual.getCompanyName()),
                    () -> Assertions.assertEquals(pointValance, actual.getPointValance())
            );
        }

        @DisplayName("실패 - 유효하지 않은 회원 정보")
        @Test
        void failure_Test_InvalidMember() {
            // Given
            Long memberId = 1L;

            // When
            Mockito.when(memberRepository.findMemberCompanyPointData(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    memberService.getMyPageData(memberId));
        }
    }

    @DisplayName("Success - 홈 화면에 필요한 정보 응답")
    @Test
    void getHomeInfoSuccess() {
        // given
        Long memberId = 1L;
        Long buildingId = 1L;
        String buildingName = "테라 타워";
        Boolean hasCafeteria = true;

        BuildingInfoProjection buildingInfoProjection = new BuildingInfoProjection(buildingId, buildingName, hasCafeteria);

        given(memberRepository.findBuildingInfoByMemberId(memberId))
            .willReturn(Optional.of(buildingInfoProjection)
            );

        // when
        BuildingInfoDto actual = memberService.getBuildingInfo(memberId);

        // then
        assertAll(
            () -> Assertions.assertEquals(buildingId, actual.getBuildingId()),
            () -> Assertions.assertEquals(buildingName, actual.getBuildingName()),
            () -> Assertions.assertEquals(hasCafeteria, actual.getHasCafeteria())
        );

    }

    @DisplayName("FAILED : 홈 화면에 필요한 정보 응답 - 유효 하지 않은 정보")
    @Test
    void getHomeInfoFailed() {
        // given
        Long memberId = 1L;

        // when
        Mockito.when(memberRepository.findBuildingInfoByMemberId(anyLong()))
            .thenReturn(Optional.empty());

        // then
        assertThrows(GlobalRuntimeException.class, () ->
            memberService.getBuildingInfo(memberId));
    }

    @DisplayName("SUCCESS - 출입 카드 정보 응답 서비스 테스트")
    @Test
    void getAccessCardSuccess() {
        // given
        String buildingName = "테라 타워";
        String employeeNumber = "123456";
        String companyName = "OFFICE 01";
        String floor = "1층";
        String roomNumber = "101호" ;
        String employeeName = "TestUser";

        AccessCardProjection accessCardProjection = new AccessCardProjection(buildingName, employeeNumber, companyName, floor, roomNumber, employeeName);
        List<AccessCardProjection> accessCardProjectionList = new ArrayList<>();
        accessCardProjectionList.add(accessCardProjection);

        given(memberRepository.findAccessCardData(anyLong()))
            .willReturn(accessCardProjectionList);

        // when
        AccessCardDto actual = memberService.getAccessCardData(anyLong());

        // then
        assertAll(
            () -> Assertions.assertEquals(buildingName, actual.getBuildingName()),
            () -> Assertions.assertEquals(employeeNumber, actual.getEmployeeNumber()),
            () -> Assertions.assertEquals(companyName, actual.getCompanyName()),
            () -> Assertions.assertEquals(floor, actual.getFloor()),
            () -> Assertions.assertEquals(roomNumber, actual.getRoomNumber()),
            () -> Assertions.assertEquals(employeeName, actual.getEmployeeName())
        );
    }

    @DisplayName("FAILED - 출입 카드 정보 응답 서비스 테스트 - 조회 실패")
    @Test
    void getAccessCardFail() {
        // given
        Long memberId = 1L;

        // when
        Mockito.when(memberRepository.findAccessCardData(anyLong()))
            .thenReturn(new ArrayList<>());

        // then
        Assertions.assertThrows(GlobalRuntimeException.class, () ->
            memberService.getAccessCardData(memberId));
    }

}
