package com.livable.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class MyPageProjection {

    String memberName;
    String companyName;
    Integer pointValance;
}
