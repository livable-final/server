package com.livable.server.menu.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.livable.server.menu.dto.MenuChoiceProjection;
import com.livable.server.menu.repository.MenuChoiceLogRepository;
import com.livable.server.menu.repository.MenuChoiceResultRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuChoiceResultServiceTest {

  	@InjectMocks
	MenuChoiceResultService menuChoiceResultService;

	@Mock
  	MenuChoiceLogRepository menuChoiceLogRepository;

	@Mock
	MenuChoiceResultRepository menuChoiceResultRepository;

	@DisplayName("SUCCESS - 가장 많이 선택한 메뉴 통계 집계 성공")
  	@Test
  	void MenuChoiceResultSuccess() {
		// given
		List<MenuChoiceProjection> menuChoiceLogs = new ArrayList<>();
		given(menuChoiceLogRepository.findMenuChoiceLog(any(LocalDate.class))).willReturn(menuChoiceLogs);

		// when
		menuChoiceResultService.createDailyMenuChoiceResult(LocalDate.now());

		// then
		verify(menuChoiceResultRepository, times(1)).saveAll(anyList());
	}

}
