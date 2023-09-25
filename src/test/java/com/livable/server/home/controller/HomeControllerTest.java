package com.livable.server.home.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.home.dto.HomeResponse.BuildingInfoDto;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

		@Autowired
		MockMvc mockMvc;

		@MockBean
		private MemberService memberService;

		@DisplayName("SUCCESS : 홈 화면에 필요한 정보 응답 컨트롤러 테스트")
		@Test
		void getHomeInfoSuccess() throws Exception {
			// given
			Long memberId = 1L;

			Long buildingId = 1L;
			String buildingName = "테라 타워";
			Boolean hasCafeteria = true;

			BuildingInfoDto buildingInfoDto = BuildingInfoDto.builder()
					.buildingId(buildingId)
					.buildingName(buildingName)
					.hasCafeteria(hasCafeteria).build();

			given(memberService.getBuildingInfo(memberId))
					.willReturn(buildingInfoDto);

			// when & then
			mockMvc.perform(get("/api/home"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.data['buildingId']").value(1))
					.andExpect(jsonPath("$.data['buildingName']").value("테라 타워"))
					.andExpect(jsonPath("$.data['hasCafeteria']").value(true));
		}

		@DisplayName("FAILED : 홈 화면에 필요한 정보 응답 컨트롤러 테스트 - 조회 실패")
		@Test
		void getHomeInfoFailed() throws Exception {
			// given
			given(memberService.getBuildingInfo(anyLong()))
					.willThrow(new GlobalRuntimeException(MemberErrorCode.BUILDING_INFO_NOT_EXIST));

			// when & then
			mockMvc.perform(get("/api/home"))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message").value(MemberErrorCode.BUILDING_INFO_NOT_EXIST.getMessage()));
		}

}
