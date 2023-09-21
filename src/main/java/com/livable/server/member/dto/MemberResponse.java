package com.livable.server.member.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    @Getter
    @Builder
    public static class MyPageDTO {

        String memberName;
        String companyName;
        Integer pointValance;

        public static MyPageDTO from(MyPageProjection myPageProjection) {
            return MyPageDTO.builder()
                    .memberName(myPageProjection.getMemberName())
                    .companyName(myPageProjection.getCompanyName())
                    .pointValance(myPageProjection.getPointValance())
                    .build();
        }
    }
}
