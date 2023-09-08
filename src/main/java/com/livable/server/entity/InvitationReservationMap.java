package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "invitation_reservation_map",
        uniqueConstraints =
        @UniqueConstraint(
                name = "INVITATION_RESERVATION_UNIQUE_IDX",
                columnNames = {"invitation_id", "reservation_id"}
        )
)
@Entity
public class InvitationReservationMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Invitation invitation;

    @JoinColumn(nullable = false, unique = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;
}
