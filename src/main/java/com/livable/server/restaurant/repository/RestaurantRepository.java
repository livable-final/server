package com.livable.server.restaurant.repository;

import com.livable.server.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantCustomRepository {
}
