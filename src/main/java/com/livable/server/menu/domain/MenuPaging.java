package com.livable.server.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuPaging {

	MOST_SELECTED_MENU(10);

	private final Integer limit;

}
