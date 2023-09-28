package com.livable.server.menu.service;

import com.livable.server.core.exception.ErrorCode;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.menu.domain.MenuErrorCode;
import com.livable.server.menu.dto.MenuResponse.MostSelectedMenuDTO;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.dto.MostSelectedMenuProjection;
import com.livable.server.menu.dto.RouletteMenu;
import com.livable.server.menu.dto.RouletteMenuProjection;
import com.livable.server.menu.repository.MenuRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MenuService {

	private final MenuRepository menuRepository;

	public List<RouletteMenuDTO> getRouletteMenus() {

		List<RouletteMenuProjection> rouletteMenuProjections = menuRepository.findRouletteMenus();

				isValidateRouletteMenus(rouletteMenuProjections, MenuErrorCode.RETRIEVE_ROULETTE_MENU_FAILED);

				Map<String, List<RouletteMenu>> rouletteMenuMap =  getMenusGroupByMenuCategory(rouletteMenuProjections);

		return convertToDTO(rouletteMenuMap);
	}

	private void isValidateRouletteMenus(List<?> projections, ErrorCode errorCode) {
		if (projections.isEmpty()) {
			throw new GlobalRuntimeException((errorCode));
		}
	}

	private  Map<String, List<RouletteMenu>> getMenusGroupByMenuCategory(List<RouletteMenuProjection> rouletteMenuProjections){

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

		if (pageable.getPageSize() == 0) {
		  	throw new GlobalRuntimeException(MenuErrorCode.BAD_PAGING_REQUEST);
		}

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
  
}
