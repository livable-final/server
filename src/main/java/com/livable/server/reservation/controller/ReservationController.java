package com.livable.server.reservation.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.reservation.dto.ReservationResponse;
import com.livable.server.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/places/{commonPlaceId}")
    public ResponseEntity<ApiResponse.Success<Object>> findAvailableTimes(
            @PathVariable Long commonPlaceId,
            @RequestParam("date") LocalDate localDate,
            @LoginActor Actor actor
            ) {

        JwtTokenProvider.checkMemberToken(actor);

        List<ReservationResponse.AvailableReservationTimePerDateDto> result =
                reservationService.findAvailableReservationTimes(actor.getId(), commonPlaceId, localDate);
        return ApiResponse.success(result, HttpStatus.OK);
    }
}
