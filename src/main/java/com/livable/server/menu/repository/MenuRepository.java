package com.livable.server.menu.repository;

import com.livable.server.entity.Menu;
import com.livable.server.menu.dto.MostSelectedMenuProjection;
import com.livable.server.menu.dto.RouletteMenuProjection;
import java.time.LocalDate;
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
        "SELECT new com.livable.server.menu.dto.MostSelectedMenuProjection(mcwr.count, mcwr.date, mcwr.menu.id, m.name,  m.representativeImageUrl) " +
        "FROM MenuChoiceWeeklyResult mcwr " +
        "JOIN Menu m " +
        "ON m.id = mcwr.menu.id " +
        "WHERE mcwr.building.id = :buildingId AND mcwr.date = :referenceDate " +
        "GROUP BY mcwr.date, mcwr.menu.id, m.name, m.representativeImageUrl " +
        "ORDER BY mcwr.count DESC "
    )
    List<MostSelectedMenuProjection> findMostSelectedMenuOrderByCount(@Param("buildingId") Long buildingId, @Param("referenceDate") LocalDate referenceDate, Pageable pageable);

    @Query(
        "SELECT m " +
        "FROM Menu m " +
        "WHERE m.id in :menuList"
    )
    List<Menu> findAllMenuByMenuId(@Param("menuList") List<Long> menuList);
}
