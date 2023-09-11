package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reservation",
        uniqueConstraints =
        @UniqueConstraint(
                name = "COMMON_PLACE_DATE_TIME_UNIQUE_IDX",
                columnNames = {"common_place_id", "date", "time"}
        )
)
@Entity
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @JoinColumn(nullable = false, name = "common_place_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CommonPlace commonPlace;

    @Column(nullable = false, name = "date")
    private LocalDate date;

    @Column(nullable = false, name = "time")
    private LocalTime time;
}
