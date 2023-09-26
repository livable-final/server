package com.livable.server.menu.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.menu.domain.MenuErrorCode;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.service.MenuService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestConfig.class)
@WebMvcTest(MenuController.class)
class MenuControllerTest {

	@Autowired
	MockMvc mockMvc;

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

		given(menuService.getRouletteMenus(anyLong()))
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

		given(menuService.getRouletteMenus(anyLong()))
				.willThrow(new GlobalRuntimeException(MenuErrorCode.RETRIEVE_ROULETTE_MENU_FAILED));

		//when & then
		mockMvc.perform(get("/api/menus")
						.header("Authorization", "Bearer " + token)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(MenuErrorCode.RETRIEVE_ROULETTE_MENU_FAILED.getMessage()));
	}

}