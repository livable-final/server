package com.livable.server.menu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouletteMenu {

	private Long menuId;
	private String name;

	public static RouletteMenu from(RouletteMenuProjection rouletteMenuProjection) {
			return RouletteMenu.builder()
					.menuId(rouletteMenuProjection.getMenuId())
					.name(rouletteMenuProjection.getName())
					.build();
	}

}
