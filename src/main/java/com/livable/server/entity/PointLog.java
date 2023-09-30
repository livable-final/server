package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PointLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Point point;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointCode code;

    @Column(nullable = false)
    private Integer amount;

    public boolean isCreated(LocalDate date) {
        LocalDateTime createdAt = this.getCreatedAt();
        LocalDate createdDate = LocalDate.of(createdAt.getYear(), createdAt.getMonth(), createdAt.getDayOfMonth());

        return createdDate.equals(date);
    }
}
