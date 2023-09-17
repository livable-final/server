package com.livable.server.visitation.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.visitation.service.VisitationFacadeService;
import com.livable.server.visitation.dto.VisitationRequest;
import com.livable.server.visitation.dto.VisitationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/visitation")
public class VisitationController {

    private final VisitationFacadeService visitationFacadeService;

    @GetMapping("/qr")
    public ResponseEntity<ApiResponse.Success<Object>> getQrCode() {

        Long visitorId = 1L;
        VisitationResponse.InvitationTimeDto invitationTime = visitationFacadeService.findInvitationTime(visitorId);
        String base64QrCode = visitationFacadeService.createQrCode(invitationTime.getStartDateTime().minusHours(1), invitationTime.getEndDateTime().plusHours(1));

        return ApiResponse.success(base64QrCode, HttpStatus.OK);
    }

    @PostMapping("/qr")
    public ResponseEntity<ApiResponse.Success<Object>> validateQrCode(@RequestBody VisitationRequest.ValidateQrDto validateQrDto) {

        visitationFacadeService.validateQrCode(validateQrDto.getQr());

        return ApiResponse.success(HttpStatus.OK);
    }
}
