package com.livable.server.home.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.home.dto.HomeResponse.BuildingInfoDto;
import com.livable.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/home")
@RestController
public class HomeController {

	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<ApiResponse.Success<BuildingInfoDto>> getHomeInfo(@LoginActor Actor actor) {

		JwtTokenProvider.checkMemberToken(actor);

		Long memberId = actor.getId();
		BuildingInfoDto buildingInfoDto = memberService.getBuildingInfo(memberId);

		return ApiResponse.success(buildingInfoDto, HttpStatus.OK);
	}

}
