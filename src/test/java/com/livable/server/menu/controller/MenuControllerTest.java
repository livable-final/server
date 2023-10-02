package com.livable.server.menu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.menu.domain.MenuErrorCode;
import com.livable.server.menu.dto.MenuRequest.MenuChoiceLogDTO;
import com.livable.server.menu.dto.MenuResponse.MostSelectedMenuDTO;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.service.MenuService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Import(TestConfig.class)
@WebMvcTest(MenuController.class)
class MenuControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	JwtTokenProvider tokenProvider;

	@MockBean
	MenuService menuService;

	@DisplayName("SUCCESS - 룰렛에 사용되는 카테고리, 메뉴 응답 컨트롤러 테스트")
	@Test
	void getRouletteMenusSuccess() throws Exception {
		//given
		String token = tokenProvider.createActorToken(
				ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

		List<RouletteMenuDTO> mockResponse = new ArrayList<>();

		given(menuService.getRouletteMenus())
				.willReturn(mockResponse);

		//when & then
		mockMvc.perform(
						get("/api/menus")
								.header("Authorization", "Bearer " + token)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@DisplayName("FAIELD - 룰렛에 사용되는 카테고리, 메뉴 응답 컨트롤러 테스트")
	@Test
	void getRouletteMenusFailed() throws Exception {
		//given
		String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

		List<RouletteMenuDTO> mockResponse = new ArrayList<>();

		given(menuService.getRouletteMenus())
				.willThrow(new GlobalRuntimeException(MenuErrorCode.RETRIEVE_ROULETTE_MENU_FAILED));

		//when & then
		mockMvc.perform(get("/api/menus")
						.header("Authorization", "Bearer " + token)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(MenuErrorCode.RETRIEVE_ROULETTE_MENU_FAILED.getMessage()));
	}

	@DisplayName("SUCCESS - 가장 많이 선택한 메뉴 10위까지 응답 컨트롤러 테스트")
	@Test
	void getMostSelectedMenusSuccess() throws Exception {
	  	//given
		String token = tokenProvider.createActorToken(
			ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

	  	Pageable pageable = PageRequest.of(0, 1);

		List<MostSelectedMenuDTO> mockResponse = new ArrayList<>();

		given(menuService.getMostSelectedMenu(1L, pageable))
			.willReturn(mockResponse);

		//when & then
		mockMvc.perform(
			  get("/api/menus/buildings/{buildingId}", 1)
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}



	@DisplayName("SUCCESS - 룰렛 선택 결과 저장 컨트롤러 테스트 : 정상")
	@Test
	void createMenuChoiceLogSuccess() throws Exception {
		//given
		Long memberId = 1L;

		String token = tokenProvider.createActorToken(
			ActorType.MEMBER, memberId, new Date(new Date().getTime() + 10000000));

		MenuChoiceLogDTO menuChoiceLogDTO = new MenuChoiceLogDTO(1L, LocalDate.now());

		doAnswer(invocation -> {
		  return null;
		}).when(menuService).createMenuChoiceLog(anyLong(), any(MenuChoiceLogDTO.class));


		//when & then
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/menus/choices")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(menuChoiceLogDTO)))
			.andExpect(status().isCreated());
	}

	@DisplayName("FAILED - 룰렛 선택 결과 저장 컨트롤러 테스트 : 오늘이 아닌 날짜 요청")
	@Test
	void createMenuChoiceLogWithInvalidDate() throws Exception {
	  //given
	  Long memberId = 1L;

	  String token = tokenProvider.createActorToken(
		  ActorType.MEMBER, memberId, new Date(new Date().getTime() + 10000000));

	  LocalDate pastDate = LocalDate.now().minusDays(1);

	  MenuChoiceLogDTO invalidDto = new MenuChoiceLogDTO(1L, pastDate);

	  doThrow(ConstraintViolationException.class)
		  .when(menuService)
		  .createMenuChoiceLog(anyLong(), any(MenuChoiceLogDTO.class));

	  //when & then
	  mockMvc.perform(
			  MockMvcRequestBuilders.post("/api/menus/choices")
				  .header("Authorization", "Bearer " + token)
				  .contentType(MediaType.APPLICATION_JSON)
				  .content(objectMapper.writeValueAsString(invalidDto)))
		  .andExpect(status().isBadRequest());
	}

	@DisplayName("FAILED - 룰렛 선택 결과 저장 컨트롤러 테스트 : 잘못된 요청 - 존재 하지 않는 유저")
	@Test
	void createMenuChoiceLogInvalidMember() throws Exception {
	  //given

	  String token = tokenProvider.createActorToken(
		  ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

	  MenuChoiceLogDTO invalidDto = new MenuChoiceLogDTO(1L, LocalDate.now());

	  doThrow(new GlobalRuntimeException(MemberErrorCode.MEMBER_NOT_EXIST))
		  .when(menuService)
		  .createMenuChoiceLog(anyLong(), any(MenuChoiceLogDTO.class));

	  //when & then
	  mockMvc.perform(
			  MockMvcRequestBuilders.post("/api/menus/choices")
				  .header("Authorization", "Bearer " + token)
				  .contentType(MediaType.APPLICATION_JSON)
				  .content(objectMapper.writeValueAsString(invalidDto)))
		  .andExpect(status().isBadRequest());
	}

	@DisplayName("FAILED - 룰렛 선택 결과 저장 컨트롤러 테스트 : 잘못된 요청 - 존재 하지 않는 메뉴")
	@Test
	void createMenuChoiceLogInvalidMenu() throws Exception {
	  //given

	  String token = tokenProvider.createActorToken(
		  ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

	  MenuChoiceLogDTO invalidDto = new MenuChoiceLogDTO(1L, LocalDate.now());

	  doThrow(new GlobalRuntimeException(MenuErrorCode.MENU_NOT_EXIST))
		  .when(menuService)
		  .createMenuChoiceLog(anyLong(), any(MenuChoiceLogDTO.class));

	  //when & then
	  mockMvc.perform(
			  MockMvcRequestBuilders.post("/api/menus/choices")
				  .header("Authorization", "Bearer " + token)
				  .contentType(MediaType.APPLICATION_JSON)
				  .content(objectMapper.writeValueAsString(invalidDto)))
		  .andExpect(status().isBadRequest());
	}

}