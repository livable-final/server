package com.livable.server.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @ColumnDefault("'USER'")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String businessCardImage;

    @Column(nullable = false, unique = true)
    private String contact;

    @Column(nullable = false)
    private String name;


    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;




}
