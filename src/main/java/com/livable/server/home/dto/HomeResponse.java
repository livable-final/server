package com.livable.server.home.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HomeResponse {

	@Getter
	@AllArgsConstructor
	public static class BuildingInfoDto {

		private Long buildingId;
		private String buildingName;
		private Boolean hasCafeteria;
	}

}
