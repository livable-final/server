package com.livable.server.menu.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.dto.RouletteMenu;
import com.livable.server.menu.dto.RouletteMenuProjection;
import com.livable.server.menu.repository.MenuRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@InjectMocks
	MenuService menuService;

	@Mock
	MenuRepository menuRepository;

	@DisplayName("SUCCESS - 룰렛에 사용되는 카테고리, 메뉴 응답 서비스 테스트")
	@Test
	void getRouletteMenusSuccess() {

		//given
		String categoryName = "카테고리1";
		String menuName = "메뉴1";

		RouletteMenuProjection rouletteMenuProjection = new RouletteMenuProjection(1L, menuName, categoryName);

		List<RouletteMenuProjection> rouletteMenuProjections = new ArrayList<>();
		rouletteMenuProjections.add(rouletteMenuProjection);

		RouletteMenu rouletteMenu = RouletteMenu.from(rouletteMenuProjection);

		List<RouletteMenu> result = new ArrayList<>();
		result.add(rouletteMenu);

		RouletteMenuDTO expected = new RouletteMenuDTO(categoryName, result);

		given(menuRepository.findRouletteMenus(anyLong()))
				.willReturn(rouletteMenuProjections);

		//when
		List<RouletteMenuDTO> actual =
				menuService.getRouletteMenus(1L);

		//then
		then(menuRepository).should(times(1)).findRouletteMenus(anyLong());
		assertAll(
				() -> assertEquals(expected.getCategoryName(), actual.get(0).getCategoryName()),
				() -> assertEquals(expected.getMenus().size(), actual.get(0).getMenus().size())
		);
	}

	@DisplayName("FAIELD - 룰렛에 사용되는 카테고리, 메뉴 응답 서비스 테스트")
	@Test
	void getRouletteMenusFailed() {

		//given
		given(menuRepository.findRouletteMenus(anyLong()))
				.willReturn(new ArrayList<>());

		//when
		assertThrows(GlobalRuntimeException.class, () ->
				menuService.getRouletteMenus(1l));
	}
}