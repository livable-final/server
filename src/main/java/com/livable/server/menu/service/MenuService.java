package com.livable.server.menu.service;

import com.livable.server.core.exception.ErrorCode;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Building;
import com.livable.server.entity.Company;
import com.livable.server.entity.Member;
import com.livable.server.entity.Menu;
import com.livable.server.entity.MenuChoiceLog;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.menu.domain.MenuErrorCode;
import com.livable.server.menu.dto.MenuRequest.MenuChoiceLogDTO;
import com.livable.server.menu.dto.MenuResponse.MostSelectedMenuDTO;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.dto.MostSelectedMenuProjection;
import com.livable.server.menu.dto.RouletteMenu;
import com.livable.server.menu.dto.RouletteMenuProjection;
import com.livable.server.menu.repository.MenuChoiceLogRepository;
import com.livable.server.menu.repository.MenuRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuChoiceLogRepository menuChoiceLogRepository;
	private final MemberRepository memberRepository;

	public List<RouletteMenuDTO> getRouletteMenus() {

		List<RouletteMenuProjection> rouletteMenuProjections = menuRepository.findRouletteMenus();

		isValidateRouletteMenus(rouletteMenuProjections, MenuErrorCode.RETRIEVE_ROULETTE_MENU_FAILED);

		Map<String, List<RouletteMenu>> rouletteMenuMap = getMenusGroupByMenuCategory(rouletteMenuProjections);

		return convertToDTO(rouletteMenuMap);
	}

	private void isValidateRouletteMenus(List<?> projections, ErrorCode errorCode) {
		if (projections.isEmpty()) {
			throw new GlobalRuntimeException((errorCode));
		}
	}

	private Map<String, List<RouletteMenu>> getMenusGroupByMenuCategory(List<RouletteMenuProjection> rouletteMenuProjections){

		Map<String, List<RouletteMenu>> menuGroupByMenuCategoryMap = new LinkedHashMap<>();

		for (RouletteMenuProjection rouletteMenuProjection : rouletteMenuProjections) {

			String menuCategoryName = rouletteMenuProjection.getMenuCategoryName();

			RouletteMenu rouletteMenus = RouletteMenu.from(rouletteMenuProjection);

			menuGroupByMenuCategoryMap.computeIfAbsent(
					menuCategoryName, k -> new ArrayList<>())
					.add(rouletteMenus);
		}

		return menuGroupByMenuCategoryMap;
	}

	private List<RouletteMenuDTO> convertToDTO(Map<String, List<RouletteMenu>> menuGroupByMenuCategoryMap) {

		List<RouletteMenuDTO> rouletteMenuDTOS = new ArrayList<>();

		menuGroupByMenuCategoryMap.forEach((key, value) ->
			rouletteMenuDTOS.add(new RouletteMenuDTO(key, value))
		);

		return rouletteMenuDTOS;
	}
    public List<MostSelectedMenuDTO> getMostSelectedMenu(Long buildingId, Pageable pageable) {

		List<MostSelectedMenuProjection> mostSelectedMenuProjections = menuRepository.findMostSelectedMenuOrderByCount(buildingId, pageable);

		return convertToDTO(mostSelectedMenuProjections);
	}

	private List<MostSelectedMenuDTO> convertToDTO(List<MostSelectedMenuProjection> mostSelectedMenuProjections) {

		List<MostSelectedMenuDTO> mostSelectedMenus = new ArrayList<>();

		for (int i = 0; i < mostSelectedMenuProjections.size(); i++) {
		  int rank = i + 1;
		  MostSelectedMenuDTO mostSelectedMenuDTO = MostSelectedMenuDTO.from(mostSelectedMenuProjections.get(i), rank);
		  mostSelectedMenus.add(mostSelectedMenuDTO);
		}

		return mostSelectedMenus;
	}

	@Transactional
	public void createMenuChoiceLog(Long memberId, MenuChoiceLogDTO menuChoiceLogDTO) {

	  	Optional<MenuChoiceLog> menuChoiceLogOptional = findMenuChoiceLogOfToday(memberId);

		MenuChoiceLog menuChoiceLog = getMenuchoiceLog(memberId, menuChoiceLogDTO);

		if(menuChoiceLogOptional.isPresent()) {
			menuChoiceLog = menuChoiceLogOptional.get();
			updateMenuChoiceLog(menuChoiceLog, menuChoiceLogDTO);
		}

		menuChoiceLogRepository.save(menuChoiceLog);

	}

	private void updateMenuChoiceLog(MenuChoiceLog menuChoiceLog, MenuChoiceLogDTO menuChoiceLogDTO) {
		Long selectedId = menuChoiceLogDTO.getMenuId();

		Menu selectedMenu = menuRepository.findById(selectedId)
			.orElseThrow(() -> new GlobalRuntimeException(MenuErrorCode.MENU_NOT_EXIST));

		menuChoiceLog.updateMenu(selectedMenu);
	}

	private MenuChoiceLog getMenuchoiceLog(Long memberId, MenuChoiceLogDTO menuChoiceLogDTO) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalRuntimeException(
				MemberErrorCode.MEMBER_NOT_EXIST));

		Company company = member.getCompany();

		Building building = company.getBuilding();

		Long menuId = menuChoiceLogDTO.getMenuId();

		Menu menu = getMenu(menuId);

		LocalDate date = menuChoiceLogDTO.getDate();

		return MenuChoiceLog.builder()
			.member(member)
			.building(building)
			.menu(menu)
			.date(date)
			.build();
	}

	private Menu getMenu(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new GlobalRuntimeException(
				MenuErrorCode.MENU_NOT_EXIST)
			);
	}

	public Optional<MenuChoiceLog> findMenuChoiceLogOfToday(Long memberId) {

		return menuChoiceLogRepository.findByMemberIdAndDate(memberId, LocalDate.now());
	}
}
