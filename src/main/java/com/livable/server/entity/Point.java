package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private Integer balance;
}
