package com.livable.server.menu.repository;

import com.livable.server.entity.MenuChoiceLog;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuChoiceLogRepository extends JpaRepository<MenuChoiceLog, Long> {

  Optional<MenuChoiceLog> findByMemberIdAndDate(Long memberId, LocalDate date);

}
