package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "building_restaurant_map",
        uniqueConstraints =
        @UniqueConstraint(
                name = "BUILDING_RESTAURANT_UNIQUE_IDX",
                columnNames = {"building_id", "restaurant_id"}
        )
)
@Entity
public class BuildingRestaurantMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false, name = "building_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @JoinColumn(nullable = false, name = "restaurant_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Boolean inBuilding;

    @Column(nullable = false)
    private Integer distance;
}
