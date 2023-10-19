package com.livable.server.menu.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Building;
import com.livable.server.entity.Company;
import com.livable.server.entity.Member;
import com.livable.server.entity.Menu;
import com.livable.server.entity.MenuChoiceLog;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.menu.domain.MenuChoiceResultDateRange;
import com.livable.server.menu.domain.MenuErrorCode;
import com.livable.server.menu.dto.MenuRequest.MenuChoiceLogDTO;
import com.livable.server.menu.dto.MenuResponse.MostSelectedMenuDTO;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.dto.MostSelectedMenuProjection;
import com.livable.server.menu.dto.RouletteMenu;
import com.livable.server.menu.dto.RouletteMenuProjection;
import com.livable.server.menu.repository.MenuChoiceLogRepository;
import com.livable.server.menu.repository.MenuRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@InjectMocks
	MenuService menuService;

	@Mock
	MenuRepository menuRepository;
	@Mock
	MenuChoiceLogRepository menuChoiceLogRepository;
	@Mock
	MemberRepository memberRepository;

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
		int pageLimit = 1;
		Pageable pageable = PageRequest.of(5, pageLimit);
		MenuChoiceResultDateRange referenceDate = MenuChoiceResultDateRange.getDateRange(date);


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

		given(menuRepository.findMostSelectedMenuOrderByCount(buildingId, referenceDate.getEndDate(), pageable))
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

	@DisplayName("SUCCESS - 룰렛 선택 결과 저장 서비스 테스트 : 당일 첫 룰렛 선택 결과 반영")
	@Test
	void createMenuChoiceLog() {
		//given
		Long memberId = 1L;
		MenuChoiceLogDTO menuChoiceLogDTO = new MenuChoiceLogDTO(1L, LocalDate.now());

		Member mockMember = mock(Member.class);
		Company mockCompany = mock(Company.class);
		Building mockBuilding = mock(Building.class);
		Menu mockMenu = mock(Menu.class);
		MenuChoiceLog mockMenuChoiceLog = mock(MenuChoiceLog.class);

		when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));
		when(menuRepository.findById(anyLong())).thenReturn(Optional.of(mockMenu));
		when(mockMember.getCompany()).thenReturn(mockCompany);
		when(mockCompany.getBuilding()).thenReturn(mockBuilding);
		when(menuChoiceLogRepository.save(any(MenuChoiceLog.class))).thenReturn(mockMenuChoiceLog);

		//when
		menuService.createMenuChoiceLog(memberId, menuChoiceLogDTO);

		//then
	  	verify(menuChoiceLogRepository).save(any(MenuChoiceLog.class));
	}

	@DisplayName("SUCCESS - 룰렛 선택 결과 저장 서비스 테스트 : 룰렛 선택 결과 업데이트 반영")
	@Test
	void updateMenuChoiceLog() {
		//given
		Long memberId = 1L;
		MenuChoiceLogDTO menuChoiceLogDTO = new MenuChoiceLogDTO(1L, LocalDate.now());

		Member mockMember = mock(Member.class);
		Company mockCompany = mock(Company.class);
		Building mockBuilding = mock(Building.class);
		Menu mockMenu = mock(Menu.class);
		MenuChoiceLog mockExistingMenuChoiceLog = mock(MenuChoiceLog.class);


		when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));
		when(menuRepository.findById(anyLong())).thenReturn(Optional.of(mockMenu));
		when(mockMember.getCompany()).thenReturn(mockCompany);
		when(mockCompany.getBuilding()).thenReturn(mockBuilding);
		when(menuChoiceLogRepository.findByMemberIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(Optional.of(mockExistingMenuChoiceLog));

		//when
		menuService.createMenuChoiceLog(memberId, menuChoiceLogDTO);

		//then
		verify(menuChoiceLogRepository).findByMemberIdAndDate(anyLong(), any(LocalDate.class));
		verify(menuChoiceLogRepository).save(any(MenuChoiceLog.class));
	}

	@DisplayName("FAILED - 룰렛 선택 결과 저장 서비스 테스트 : 잘못된 요청 - 존재 하지 않는 유저")
	@Test
	void createMenuChoiceLogInvalidMember() {
		//given
		Long memberId = 1L;
		MenuChoiceLogDTO menuChoiceLogDTO = new MenuChoiceLogDTO(1L, LocalDate.now());

		//when & then
		GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () ->
			menuService.createMenuChoiceLog(memberId, menuChoiceLogDTO));
		assertEquals(MemberErrorCode.MEMBER_NOT_EXIST, globalRuntimeException.getErrorCode());
	}

	@DisplayName("FAILED - 룰렛 선택 결과 저장 서비스 테스트 : 잘못된 요청 - 존재 하지 않는 메뉴")
	@Test
	void createMenuChoiceLogInvalidMenu() {
		//given
		Long memberId = 1L;
		MenuChoiceLogDTO menuChoiceLogDTO = new MenuChoiceLogDTO(1L, LocalDate.now());

		Member mockMember = mock(Member.class);
		Company mockCompany = mock(Company.class);
		Building mockBuilding = mock(Building.class);

		when(mockMember.getCompany()).thenReturn(mockCompany);
		when(mockCompany.getBuilding()).thenReturn(mockBuilding);
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));

		//when & then
		GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () ->
			menuService.createMenuChoiceLog(memberId, menuChoiceLogDTO));
		assertEquals(MenuErrorCode.MENU_NOT_EXIST, globalRuntimeException.getErrorCode());
	}

}