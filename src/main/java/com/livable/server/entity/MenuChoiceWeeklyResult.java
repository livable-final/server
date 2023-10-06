package com.livable.server.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "menu_choice_weekly_result",
        uniqueConstraints =
        @UniqueConstraint(
                name = "MENU_BUILDING_DATE_UNIQUE_IDX",
                columnNames = {"building_id", "menu_id", "date"}
        )
)
@Entity
public class MenuChoiceWeeklyResult extends BaseTimeEntity {

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
    private Integer count;
}
