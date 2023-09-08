package com.livable.server.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "menu_choice_result",
        uniqueConstraints =
        @UniqueConstraint(
                name = "MEMBER_BUILDING_DATE_UNIQUE_IDX",
                columnNames = {"building_id", "menu_id", "date"}
        )
)
@Entity
public class MenuChoiceResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false, name = "building_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @JoinColumn(nullable = false, name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(nullable = false, name = "date")
    private LocalDate date;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer count;
}
