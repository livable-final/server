package com.livable.server.member.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
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
    public ResponseEntity<ApiResponse.Success<MemberResponse.MyPageDTO>> myPage(@LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();
        MemberResponse.MyPageDTO myPageDTO = memberService.getMyPageData(memberId);

        return ApiResponse.success(myPageDTO, HttpStatus.OK);
    }
}
