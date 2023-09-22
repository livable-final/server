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
@Entity
public class Invitation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private String description;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private String officeName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    public void updateDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDate = startDateTime.toLocalDate();
        this.startTime = startDateTime.toLocalTime();
        this.endDate = endDateTime.toLocalDate();
        this.endTime = endDateTime.toLocalTime();
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
