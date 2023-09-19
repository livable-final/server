package com.livable.server.visitation.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.visitation.service.VisitationFacadeService;
import com.livable.server.visitation.dto.VisitationRequest;
import com.livable.server.visitation.dto.VisitationResponse;
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
    public ResponseEntity<ApiResponse.Success<Object>> findVisitationDetailInformation() {
        Long visitorId = 1L;
//        VisitationResponse.DetailInformationDto = visitationFacadeService.findVisitationDetailInformation(1L);

//        return ApiResponse.success()
        return null;
    }

    @GetMapping("/qr")
    public ResponseEntity<ApiResponse.Success<Object>> createQrCode() {

        Long visitorId = 1L;
        String base64QrCode = visitationFacadeService.createQrCode(visitorId);

        return ApiResponse.success(base64QrCode, HttpStatus.OK);
    }

    @PostMapping("/qr")
    public ResponseEntity<ApiResponse.Success<Object>> validateQrCode(@RequestBody VisitationRequest.ValidateQrDto validateQrDto) {

        visitationFacadeService.validateQrCode(validateQrDto.getQr());

        return ApiResponse.success(HttpStatus.OK);
    }

    @PostMapping("/parking")
    public ResponseEntity<ApiResponse.Success<Object>> registerParking(
            @RequestBody @Valid VisitationRequest.RegisterParkingDto registerParkingDto
    ) {
        Long visitorId = 1L;
        visitationFacadeService.registerParking(visitorId, registerParkingDto.getCarNumber());

        return ApiResponse.success(HttpStatus.CREATED);
    }

}
