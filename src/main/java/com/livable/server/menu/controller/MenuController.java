package com.livable.server.menu.controller;

import static com.livable.server.menu.domain.MenuPaging.MOST_SELECTED_MENU;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.menu.dto.MenuRequest.MenuChoiceLogDTO;
import com.livable.server.menu.dto.MenuResponse.MostSelectedMenuDTO;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.service.MenuService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/api/menus")
    public ResponseEntity<Success<List<RouletteMenuDTO>>> getRouletteMenus(@LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        List<RouletteMenuDTO> rouletteMenuDTOs = menuService.getRouletteMenus();

        return ApiResponse.success(rouletteMenuDTOs, HttpStatus.OK);
    }

    @GetMapping("/api/menus/choices")
    public ResponseEntity<Success<List<MostSelectedMenuDTO>>> getMostSelectedMenu(@RequestParam("buildingId") Long buildingId, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        //추후 가져 오는 메뉴 숫자 변경시 변경
        Pageable pageable = PageRequest.of(0, MOST_SELECTED_MENU.getLimit());


        List<MostSelectedMenuDTO> mostSelectedMenu = menuService.getMostSelectedMenu(buildingId, pageable);

        return ApiResponse.success(mostSelectedMenu, HttpStatus.OK);
    }

    @PostMapping("/api/menus/choices")
    public ResponseEntity<?> createMenuChoiceLog(@Valid @RequestBody MenuChoiceLogDTO menuChoiceLogDTO, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        //오늘 이미 룰렛을 돌렸다면 결과를 반영하지 않고 201 return
        menuService.createMenuChoiceLog(memberId, menuChoiceLogDTO);

        return ApiResponse.success(HttpStatus.CREATED);
    }

}
