package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Visitor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Invitation invitation;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column
    private LocalDateTime firstVisitedTime;

    public void updateFirstVisitedTime() {
        this.firstVisitedTime = LocalDateTime.now();
    }
}
