package com.livable.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BuildingInfoProjection {

    Long buildingId;
    String buildingName;
    Boolean hasCafeteria;
}
