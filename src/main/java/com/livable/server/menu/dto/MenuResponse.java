package com.livable.server.menu.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse {

	@Getter
	@AllArgsConstructor
	public static class RouletteMenuDTO {

		private String categoryName;
		private List<RouletteMenu> menus;

	}

}
