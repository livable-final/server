package com.livable.server.admin.repository;

import com.livable.server.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long>, AdminQueryRepository {
}
