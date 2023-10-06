package com.livable.server.menu.repository;

import com.livable.server.entity.MenuChoiceResult;
import com.livable.server.menu.dto.MenuChoiceProjection;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuChoiceResultRepository extends JpaRepository<MenuChoiceResult, Long> {
	@Query("SELECT new com.livable.server.menu.dto.MenuChoiceProjection(mcr.building, mcr.menu, mcr.date, sum(mcr.count)) " +
		"FROM MenuChoiceResult mcr " +
		"WHERE mcr.date between :startDate AND :endDate " +
		"GROUP BY mcr.building, mcr.menu, mcr.date " +
		"ORDER BY sum(mcr.count) DESC")
	List<MenuChoiceProjection> findMenuChoiceResult(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
