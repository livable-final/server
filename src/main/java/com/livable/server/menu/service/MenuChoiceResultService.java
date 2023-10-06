package com.livable.server.menu.service;

import com.livable.server.entity.MenuChoiceResult;
import com.livable.server.entity.MenuChoiceWeeklyResult;
import com.livable.server.menu.domain.MenuChoiceResultDateRange;
import com.livable.server.menu.domain.MenuPaging;
import com.livable.server.menu.dto.MenuChoiceProjection;
import com.livable.server.menu.repository.MenuChoiceLogRepository;
import com.livable.server.menu.repository.MenuChoiceResultRepository;
import com.livable.server.menu.repository.MenuChoiceWeeklyResultRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MenuChoiceResultService {

	private final MenuChoiceLogRepository menuChoiceLogRepository;
	private final MenuChoiceResultRepository menuChoiceResultRepository;
	private final MenuChoiceWeeklyResultRepository menuChoiceWeeklyResultRepository;

	@Transactional
	public void createDailyMenuChoiceResult(LocalDate referenceDate) {

		List<MenuChoiceResult> menuChoiceResults  = new ArrayList<>();
		List<MenuChoiceProjection> menuChoiceLogs = menuChoiceLogRepository.findMenuChoiceLog(referenceDate);

		for (MenuChoiceProjection menuChoiceProjection : menuChoiceLogs) {

			 MenuChoiceResult menuChoiceResult = MenuChoiceResult.builder()
				 .menu(menuChoiceProjection.getMenu())
				 .building(menuChoiceProjection.getBuilding())
				 .date(menuChoiceProjection.getDate())
				 .count((menuChoiceProjection.getCount()).intValue())
				 .build();

			  menuChoiceResults.add(menuChoiceResult);
		}

		menuChoiceResultRepository.saveAll(menuChoiceResults);

	}

	@Transactional
	public void createWeeklyMenuChoiceResult(LocalDate referenceDate) {

	  	MenuChoiceResultDateRange dateRange = MenuChoiceResultDateRange.getDateRange(referenceDate);

		LocalDate startDate = dateRange.getStartDate();
		LocalDate endDate = dateRange.getEndDate();

		List<MenuChoiceProjection> menuChoiceProjections = menuChoiceResultRepository.findMenuChoiceResult(startDate, endDate);
		List<MenuChoiceWeeklyResult> menuChoiceWeeklyResults = new ArrayList<>();

		for (MenuChoiceProjection menuChoiceProjection : menuChoiceProjections) {

		  MenuChoiceWeeklyResult menuChoiceWeeklyResult = MenuChoiceWeeklyResult.builder()
			  .menu(menuChoiceProjection.getMenu())
			  .building(menuChoiceProjection.getBuilding())
			  .date(menuChoiceProjection.getDate())
			  .count((menuChoiceProjection.getCount()).intValue())
			  .build();

		  menuChoiceWeeklyResults.add(menuChoiceWeeklyResult);
		  if (menuChoiceWeeklyResults.size() > MenuPaging.MOST_SELECTED_MENU.getLimit()) {
			  break;
		  }

		}

	  	menuChoiceWeeklyResultRepository.saveAll(menuChoiceWeeklyResults);

	}

}
