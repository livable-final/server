package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CommonPlace commonPlace;

    @Column(nullable = false, unique = true)
    private LocalDateTime time;
}
