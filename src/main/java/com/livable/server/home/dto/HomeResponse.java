package com.livable.server.home.dto;

import com.livable.server.member.dto.BuildingInfoProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HomeResponse {

		@Getter
		@Builder
		public static class BuildingInfoDto {

			private Long buildingId;
			private String buildingName;
			private Boolean hasCafeteria;

			public static BuildingInfoDto from(BuildingInfoProjection buildingInfoProjection) {
				return BuildingInfoDto.builder()
						.buildingId(buildingInfoProjection.getBuildingId())
						.buildingName(buildingInfoProjection.getBuildingName())
						.hasCafeteria(buildingInfoProjection.getHasCafeteria())
						.build();
			}
		}

}
