package com.livable.server.review.repository;

import com.livable.server.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value =
        "SELECT count(r) " +
        "FROM Review r " +
        "WHERE r.member.id = :memberId and DATE(r.createdAt) = current_date "
    )
    Long findBymemberIdAndDate(@Param("memberId") Long memberId);
}
