package com.livable.server.menu.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MostSelectedMenuProjection {

  	Integer count;
	LocalDate date;
  	Long menuId;
	String menuName;
	String menuImage;

}
