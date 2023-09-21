package com.livable.server.member.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.member.dto.MemberResponse;
import com.livable.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/members")
    public ResponseEntity<ApiResponse.Success<MemberResponse.MyPageDTO>> myPage() {

        Long memberId = 1L; // TODO: JWT 구현 후 토큰에서 값 추출 예정
        MemberResponse.MyPageDTO myPageDTO = memberService.getMyPageData(memberId);

        return ApiResponse.success(myPageDTO, HttpStatus.OK);
    }
}
