package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "review_menu_map",
        uniqueConstraints =
        @UniqueConstraint(
                name = "REVIEW_MENU_UNIQUE_IDX",
                columnNames = {"review_Id", "menu_id"}
        )
)
@Entity
public class ReviewMenuMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
}
