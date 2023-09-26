package com.livable.server.menu.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.menu.dto.MenuResponse.RouletteMenuDTO;
import com.livable.server.menu.service.MenuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/api/menus")
    public ResponseEntity<Success<List<RouletteMenuDTO>>> getRouletteMenus() {

        Long buildingId = 1L;
        List<RouletteMenuDTO> rouletteMenuDTOs = menuService.getRouletteMenus(buildingId);

        return ApiResponse.success(rouletteMenuDTOs, HttpStatus.OK);
    }

}
