package com.livable.server.invitation.repository;

import com.livable.server.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    List<Office> findAllByCompanyId(Long companyId);
}
