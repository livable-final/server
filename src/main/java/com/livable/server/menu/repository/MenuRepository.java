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

    @Query("SELECT distinct new com.livable.server.menu.dto.RouletteMenuProjection(m.id, m.name, mc.name) " +
        "FROM Menu m " +
        "JOIN MenuCategory mc " +
        "ON m.menuCategory.id = mc.id"
    )
    List<RouletteMenuProjection> findRouletteMenus(@Param("memberId") Long memberId);

}
