package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "restaurant_menu_map",
        uniqueConstraints =
        @UniqueConstraint(
                name = "RESTAURANT_MENU_UNIQUE_IDX",
                columnNames = {"restaurant_id", "menu_id"}
        )
)
@Entity
public class RestaurantMenuMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false, name = "restaurant_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @JoinColumn(nullable = false, name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
}
