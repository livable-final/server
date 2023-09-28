package com.livable.server.menu.controller;

import static com.livable.server.menu.domain.MenuPaging.MOST_SELECTED_MENU;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.menu.dto.MenuResponse.MostSelectedMenuDTO;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.service.MenuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/api/menus/buildings/{buildingId}")
    public ResponseEntity<Success<List<MostSelectedMenuDTO>>> getMostSelectedMenu(@PathVariable("buildingId") Long buildingId, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        List<MostSelectedMenuDTO> mostSelectedMenu = menuService.getMostSelectedMenu(buildingId, MOST_SELECTED_MENU.getLimit());

        return ApiResponse.success(mostSelectedMenu, HttpStatus.OK);
    }

}
