package com.livable.server.menu.dto;

import com.livable.server.entity.Building;
import com.livable.server.entity.Menu;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuChoiceProjection {

	Building building;
	Menu menu;
	LocalDate date;
	Long count;

}
