package com.livable.server.home.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.home.dto.HomeResponse.AccessCardDto;
import com.livable.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {

	private final MemberService memberService;

	@GetMapping("/api/access-card")
	public ResponseEntity<ApiResponse.Success<AccessCardDto>> getAccessCard(){

		Long memberId = 1L; // TODO: 2023-09-22 JWT Token으로 대체

		AccessCardDto accessCardDto = memberService.getAccessCardData(memberId);

		return ApiResponse.success(accessCardDto, HttpStatus.OK);
	}

}
