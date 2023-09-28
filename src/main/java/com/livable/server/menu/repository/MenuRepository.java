package com.livable.server.menu.repository;

import com.livable.server.entity.Menu;
import com.livable.server.menu.dto.MostSelectedMenuProjection;
import com.livable.server.menu.dto.RouletteMenuProjection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT distinct new com.livable.server.menu.dto.RouletteMenuProjection(m.id, m.name, mc.name) " +
        "FROM Menu m " +
        "JOIN MenuCategory mc " +
        "ON m.menuCategory.id = mc.id"
    )
    List<RouletteMenuProjection> findRouletteMenus();

    @Query(
        "SELECT new com.livable.server.menu.dto.MostSelectedMenuProjection(mcr.count, mcr.date, mcr.menu.id, m.name,  m.representativeImageUrl) " +
        "FROM MenuChoiceResult mcr " +
        "JOIN Menu m " +
        "ON m.id = mcr.menu.id " +
        "WHERE mcr.building.id = :buildingId AND mcr.date = CURRENT_DATE " +
        "GROUP BY mcr.date, mcr.menu.id, m.name, m.representativeImageUrl " +
        "ORDER BY mcr.count DESC "
    )
    List<MostSelectedMenuProjection> findMostSelectedMenuOrderByCount(@Param("buildingId") Long buildingId, Pageable pageable);
}
