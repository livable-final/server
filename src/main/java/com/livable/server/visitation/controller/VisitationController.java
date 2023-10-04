package com.livable.server.visitation.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.service.VisitationFacadeService;
import com.livable.server.visitation.dto.VisitationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/visitation")
public class VisitationController {

    private final VisitationFacadeService visitationFacadeService;

    @GetMapping
    public ResponseEntity<ApiResponse.Success<Object>> findVisitationDetailInformation(@LoginActor Actor actor) {

        JwtTokenProvider.checkVisitorToken(actor);

        Long visitorId = actor.getId();
        VisitationResponse.DetailInformationDto detailInformationDto =
                visitationFacadeService.findVisitationDetailInformation(visitorId);

        return ApiResponse.success(detailInformationDto, HttpStatus.OK);
    }

    @GetMapping("/qr")
    public ResponseEntity<ApiResponse.Success<Object>> createQrCode(@LoginActor Actor actor) {

        JwtTokenProvider.checkVisitorToken(actor);

        Long visitorId = actor.getId();
        VisitationResponse.Base64QrCode qrCode = visitationFacadeService.createQrCode(visitorId);

        return ApiResponse.success(qrCode, HttpStatus.OK);
    }

    @PostMapping("/qr")
    public ResponseEntity<ApiResponse.Success<Object>> validateQrCode(
            @RequestBody @Valid VisitationRequest.ValidateQrCodeDto validateQrCodeDto,
            @LoginActor Actor actor
    ) {
        JwtTokenProvider.checkVisitorToken(actor);

        visitationFacadeService.validateQrCode(validateQrCodeDto.getQr(), actor.getId());

        return ApiResponse.success(HttpStatus.OK);
    }

    @GetMapping("/parking")
    public ResponseEntity<ApiResponse.Success<Object>> findCarNumber(@LoginActor Actor actor) {

        JwtTokenProvider.checkVisitorToken(actor);

        Long visitorId = actor.getId();
        VisitationResponse.CarNumber result = visitationFacadeService.findCarNumber(visitorId);

        return ApiResponse.success(result, HttpStatus.OK);
    }

    @PostMapping("/parking")
    public ResponseEntity<ApiResponse.Success<Object>> registerParking(
            @RequestBody @Valid VisitationRequest.RegisterParkingDto registerParkingDto, @LoginActor Actor actor
    ) {

        JwtTokenProvider.checkVisitorToken(actor);

        Long visitorId = actor.getId();
        visitationFacadeService.registerParking(visitorId, registerParkingDto.getCarNumber());

        return ApiResponse.success(HttpStatus.CREATED);
    }
}
