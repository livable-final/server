package com.livable.server.admin.service;

import com.livable.server.admin.domain.AdminErrorCode;
import com.livable.server.admin.domain.VisitationQuery;
import com.livable.server.admin.dto.AdminResponse;
import com.livable.server.admin.repository.AdminRepository;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse;
import com.livable.server.entity.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<?> getVisitationList(
            Pageable pageable, VisitationQuery visitationQuery, Long adminId
    ) {
        visitationQuery.validate();

        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        Admin admin = optionalAdmin.orElseThrow(
                () -> new GlobalRuntimeException(AdminErrorCode.NOT_EXIST_ADMIN));

        Page<AdminResponse.ListDTO> responseBody
                = adminRepository.findVisitationWithQuery(pageable, visitationQuery, admin.getBuilding().getId());

        return ApiResponse.success(responseBody, HttpStatus.OK);
    }
}
