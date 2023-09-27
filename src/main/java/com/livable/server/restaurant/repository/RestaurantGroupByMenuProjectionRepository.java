package com.livable.server.restaurant.repository;

import com.livable.server.restaurant.dto.RestaurantByMenuProjection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantGroupByMenuProjectionRepository {

  private static final String FIND_RESTAURANT_BY_MENU_ID_QUERY;

  static {
	FIND_RESTAURANT_BY_MENU_ID_QUERY = "SELECT res.id as restaurantId, res.name as restaurantName, res.thumbnail_image_url as restaurantThumbnailUrl, res.address as address, " +
		"brm.in_building as inBuilding, brm.distance as distance, " +
		"MAX(" +
		"(SELECT r.description " +
		"     FROM review r " +
		"     WHERE r.id = rsv.id " +
		"     ORDER BY r.created_at DESC " +
		"     LIMIT 1)) as review, " +
		"(SELECT ROUND(SUM(CASE WHEN rsv2.taste = 'good' THEN 1 ELSE 0 END) / COUNT(rsv2.id) * 100, 0) " +
		"FROM restaurant_review rsv2 " +
		"WHERE rsv2.restaurant_id = res.id) as tastePercentage " +
		"FROM member m " +
		"JOIN company c " +
		"ON m.company_id = c.id " +
		"JOIN building_restaurant_map brm " +
		"ON c.building_id = brm.building_id " +
		"JOIN restaurant res ON brm.restaurant_id = res.id " +
		"JOIN restaurant_menu_map rmm " +
		"ON res.id = rmm.restaurant_id " +
		"LEFT JOIN restaurant_review rsv ON res.id = rsv.restaurant_id " +
		"WHERE rmm.menu_id = :menuId AND m.id = :memberId " +
		"GROUP BY res.id, res.name, res.thumbnail_image_url, res.address, brm.in_building, brm.distance";
  }

  @PersistenceContext
  private EntityManager entityManager;

  public List<RestaurantByMenuProjection> findRestaurantByMenuId(Long menuId, Long memberId) {

	Query query = entityManager.createNativeQuery(FIND_RESTAURANT_BY_MENU_ID_QUERY, "RestaurantsByMenuMapping")
		.setParameter("menuId", menuId)
		.setParameter("memberId", memberId);

	return (List<RestaurantByMenuProjection>) query.getResultList();
  }


}
