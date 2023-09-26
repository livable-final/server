package com.livable.server.invitation.controller;

import com.livable.server.invitation.service.InvitationValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class InvitationValidationController {

    private static final String visitationPageUrl = "https://livable.vercel.app/invitation/view";

    private final InvitationValidationService invitationValidationService;

    @GetMapping("api/invitation/callback")
    public String validateVisitor(@RequestParam String token, HttpServletResponse response) {

        invitationValidationService.validateVisitor(token);

        response.setHeader("Authorization", createBearerToken(token));

        return "redirect:" + visitationPageUrl;
    }

    private String createBearerToken(String token) {
        return "Bearer " + token;
    }
}
