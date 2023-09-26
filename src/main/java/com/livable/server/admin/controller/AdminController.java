package com.livable.server.admin.controller;

import com.livable.server.admin.domain.VisitationQuery;
import com.livable.server.admin.service.AdminService;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * http://localhost:8080/api/admin/visitation?page=10&size=1&queryCondition=COMPANY&query=sixsense&startDate=2023-09-24&endDate=2023-09-25
     * @param pageable
     * @param visitationQueryCondition
     */

    @GetMapping("/visitation")
    public ResponseEntity<?> getVisitationList(
            Pageable pageable, VisitationQuery visitationQueryCondition, @LoginActor Actor actor
    ) {

        JwtTokenProvider.checkAdminToken(actor);

        Long adminId = actor.getId();

        return adminService.getVisitationList(pageable, visitationQueryCondition, adminId);
    }
}
