package com.livable.server.menu.repository;

import com.livable.server.entity.Menu;
import com.livable.server.menu.dto.RouletteMenuProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT distinct new com.livable.server.menu.dto.RouletteMenuProjection(menu.id, menu.name, mc.name) " +
            "FROM Member m " +
            "JOIN Company c " +
            "ON c.id = m.company.id " +
            "JOIN BuildingRestaurantMap brm " +
            "ON brm.building.id = c.building.id " +
            "JOIN RestaurantMenuMap rmm " +
            "ON brm.restaurant.id = rmm.restaurant.id " +
            "JOIN Menu menu " +
            "ON rmm.menu.id = menu.id " +
            "JOIN MenuCategory mc " +
            "ON menu.menuCategory.id = mc.id " +
            "WHERE m.id = :memberId"
    )
    List<RouletteMenuProjection> findRouletteMenus(@Param("memberId") Long memberId);

}
