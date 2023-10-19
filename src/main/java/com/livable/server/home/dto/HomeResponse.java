package com.livable.server.home.dto;

import com.livable.server.member.dto.AccessCardProjection;
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

	@Getter
	@Builder
	public static class AccessCardDto {

		private String buildingName;
		private String employeeNumber;
		private String companyName;
		private String floor;
		private String roomNumber;
		private String employeeName;

		public static AccessCardDto from(AccessCardProjection accessCardProjection) {
			return AccessCardDto
					.builder()
					.buildingName(accessCardProjection.getBuildingName())
					.employeeNumber(accessCardProjection.getEmployeeNumber())
					.companyName(accessCardProjection.getCompanyName())
					.floor(accessCardProjection.getFloor())
					.roomNumber(accessCardProjection.getRoomNumber())
					.employeeName(accessCardProjection.getEmployeeName())
					.build();
		}
	}

}
