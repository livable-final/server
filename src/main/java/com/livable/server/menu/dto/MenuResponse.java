package com.livable.server.menu.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MostSelectedMenuDTO {

		private LocalDate date;
		private Integer count;
		private Integer rank;
		private Long menuId;
		private String menuName;
		private String menuImage;

		public static MostSelectedMenuDTO from(MostSelectedMenuProjection mostSelectedMenuProjection, Integer rank) {
		  return MostSelectedMenuDTO.builder()
			  .date(mostSelectedMenuProjection.getDate())
			  .count(mostSelectedMenuProjection.getCount())
			  .rank(rank)
			  .menuId(mostSelectedMenuProjection.getMenuId())
			  .menuName(mostSelectedMenuProjection.menuName)
			  .menuImage(mostSelectedMenuProjection.getMenuImage())
			  .build();
	  }

	}

}
