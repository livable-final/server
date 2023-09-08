package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "point_log",
        uniqueConstraints =
        @UniqueConstraint(
                name = "REFERENCE_UNIQUE_IDX",
                columnNames = {"reference_id", "reference_type"}
        )
)
@Entity
public class PointLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Point point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointCode code;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Long referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferenceType referenceType;
}
