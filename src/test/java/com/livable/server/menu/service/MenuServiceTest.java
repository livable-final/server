package com.livable.server.menu.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.menu.dto.MenuResponse.MostSelectedMenuDTO;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.dto.MostSelectedMenuProjection;
import com.livable.server.menu.dto.RouletteMenu;
import com.livable.server.menu.dto.RouletteMenuProjection;
import com.livable.server.menu.repository.MenuRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

		given(menuRepository.findRouletteMenus())
				.willReturn(rouletteMenuProjections);

		//when
		List<RouletteMenuDTO> actual =
				menuService.getRouletteMenus();

		//then
		then(menuRepository).should(times(1)).findRouletteMenus();
		assertAll(
				() -> assertEquals(expected.getCategoryName(), actual.get(0).getCategoryName()),
				() -> assertEquals(expected.getMenus().size(), actual.get(0).getMenus().size())
		);
	}

	@DisplayName("FAIELD - 룰렛에 사용되는 카테고리, 메뉴 응답 서비스 테스트")
	@Test
	void getRouletteMenusFailed() {

		//given
		given(menuRepository.findRouletteMenus())
				.willReturn(new ArrayList<>());

		//when
		assertThrows(GlobalRuntimeException.class, () ->
				menuService.getRouletteMenus());
	}

	@DisplayName("SUCCESS - 가장 많이 선택한 메뉴 10위까지 응답 서비스 테스트")
	@Test
	void getMostSelectedMenusSuccess() {

		//given

		Long buildingId = 1L;
		Integer count = 4;
		LocalDate date = LocalDate.now();
		Long menuId = 1L;
		String menuName = "메뉴1";
		String menuUrl = "/dummyUrl";
		Integer pageLimit = 1;
		Pageable pageable = PageRequest.of(0, pageLimit);


		MostSelectedMenuProjection mostSelectedMenuProjection = new MostSelectedMenuProjection(count,
			date, menuId, menuName, menuUrl);

		List<MostSelectedMenuProjection> mostSelectedMenuProjections = new ArrayList<>();
		mostSelectedMenuProjections.add(mostSelectedMenuProjection);

		List<MostSelectedMenuDTO> result = new ArrayList<>();

		for (int i = 0; i < mostSelectedMenuProjections.size() ; i++) {
			MostSelectedMenuDTO mostSelectedMenuDTO = MostSelectedMenuDTO.from(mostSelectedMenuProjection, i);
			result.add(mostSelectedMenuDTO);
		}

		MostSelectedMenuDTO expected = new MostSelectedMenuDTO(date, count, pageLimit, menuId, menuName, menuUrl);



		given(menuRepository.findMostSelectedMenuOrderByCount(buildingId, pageable))
			.willReturn(mostSelectedMenuProjections);

		//when
		List<MostSelectedMenuDTO> actual =
			menuService.getMostSelectedMenu(buildingId, pageable);

		//then
		assertAll(
			() -> assertEquals(expected.getCount(), actual.get(0).getCount()),
			() -> assertEquals(expected.getMenuId(), actual.get(0).getMenuId()),
			() -> assertEquals(expected.getRank(), actual.get(0).getRank()),
			() -> assertEquals(expected.getMenuName(), actual.get(0).getMenuName()),
			() -> assertEquals(expected.getMenuImage(), actual.get(0).getMenuImage()),
			() -> assertEquals(expected.getDate(), actual.get(0).getDate())
		);
	}

	@DisplayName("FAIELD - 가장 많이 선택한 메뉴 10위까지 응답 서비스 테스트")
	@Test
	void getMostSelectedMenusFailedThrowException() {

		//given

		Pageable pageable = PageRequest.of(0, 1);

		given(menuRepository.findMostSelectedMenuOrderByCount(1L, pageable))
			.willThrow(GlobalRuntimeException.class);

		//when
		assertThrows(GlobalRuntimeException.class, () ->
			menuService.getMostSelectedMenu(1L, pageable));
	}

	@DisplayName("FAIELD - 가장 많이 선택한 메뉴 10위까지 응답 서비스 테스트")
	@Test
	void getMostSelectedMenusFailedBadPagingLimit() {

		//given

		Pageable pageable = PageRequest.of(0, 1);

		given(menuRepository.findMostSelectedMenuOrderByCount(1L, pageable))
			.willThrow(GlobalRuntimeException.class);

		//when
		assertThrows(GlobalRuntimeException.class, () ->
			menuService.getMostSelectedMenu(1L, pageable));
	}
}