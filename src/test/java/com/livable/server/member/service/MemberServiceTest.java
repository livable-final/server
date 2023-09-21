package com.livable.server.member.service;

import static com.livable.server.home.dto.HomeResponse.BuildingInfoDto;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

	@DisplayName("Success - 홈 화면에 필요한 정보 응답")
	@Test
	void getHomeInfoSuccess() {
		// given
		Long memberId = 1L;
		Long buildingId = 1L;
		String buildingName = "테라 타워";
		Boolean hasCafeteria = true;

		given(memberRepository.findBuildingInfoByMemberId(memberId))
				.willReturn(Optional.of(new BuildingInfoDto(buildingId, buildingName, hasCafeteria))
				);

		// when
		BuildingInfoDto actual = memberService.getBuildingInfo(memberId);

		// then
		assertAll(
			() -> assertEquals(buildingId, actual.getBuildingId()),
			() -> assertEquals(buildingName, actual.getBuildingName()),
			() -> assertEquals(hasCafeteria, actual.getHasCafeteria())
		);

	}

	@DisplayName("FAILED : 홈 화면에 필요한 정보 응답 - 유효하지 않은 정보")
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
}