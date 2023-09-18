package com.livable.server.reservation.repository;

import com.livable.server.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationQueryRepository {
}
