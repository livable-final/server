package com.livable.server.reservation.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.reservation.domain.ReservationRequest;
import com.livable.server.reservation.dto.ReservationResponse;
import com.livable.server.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/places/{commonPlaceId}")
    public ResponseEntity<ApiResponse.Success<Object>> findAvailableTimes(
            @PathVariable Long commonPlaceId,
            @ModelAttribute ReservationRequest.DateQuery dateQuery,
            @LoginActor Actor actor
    ) {
        log.info("Request Time: {}", LocalDateTime.now());
        JwtTokenProvider.checkMemberToken(actor);

        List<ReservationResponse.AvailableReservationTimePerDateDto> result =
                reservationService.findAvailableReservationTimes(actor.getId(), commonPlaceId, dateQuery);
        if (result.size() != 0) {
            log.info("time: {}", result.get(0).getAvailableTimes().get(0).toString());
        }

        return ApiResponse.success(result, HttpStatus.OK);
    }
}
