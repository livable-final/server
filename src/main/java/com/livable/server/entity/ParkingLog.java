package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ParkingLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Visitor visitor;

    @Column(nullable = false)
    private String carNumber;

    @Column
    private LocalDateTime inTime;

    @Column
    private LocalDateTime outTime;

    @Column
    private Integer stayTime;

    public static ParkingLog create(Visitor visitor, String carNumber) {
        return ParkingLog.builder()
                .carNumber(carNumber)
                .visitor(visitor)
                .build();
    }
}
