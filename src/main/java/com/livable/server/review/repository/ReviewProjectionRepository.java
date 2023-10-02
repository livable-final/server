package com.livable.server.review.repository;

import com.livable.server.core.util.ImageSeparator;
import com.livable.server.review.dto.Projection;
import com.livable.server.review.dto.ReviewResponse;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReviewProjectionRepository {

    private static final String FIND_ALL_REVIEWS_BY_YEAR_AND_MONTH_QUERY;
    private static final String FIND_ALL_REVIEW_DETAIL_BETWEEN_DATE_QUERY;

    static {
        FIND_ALL_REVIEWS_BY_YEAR_AND_MONTH_QUERY = "select  " +
                "result.id as reviewId, " +
                "result.type as type, " +
                "result.url as reviewImageUrl, " +
                "result.created_at as reviewDate " +
                "from ( " +
                "select r.id, date_format(r.created_at, \"%Y-%m-%d\") as created_at, " +
                "(select ri3.url " +
                "from review_image ri3 " +
                "where ri3.id = min(ri.id)) as url, " +
                "'cafeteria' as type " +
                "from review r " +
                "inner join cafeteria_review cr on cr.id = r.id " +
                "left join review_image ri  " +
                "on ri.review_id = r.id " +
                "group by r.id " +
                "union " +
                "select r.id, date_format(r.created_at, \"%Y-%m-%d\") as created_at, " +
                "(select ri3.url " +
                "from review_image ri3 " +
                "where ri3.id = min(ri.id)) as url, " +
                "'lunchbox' as type " +
                "from review r " +
                "inner join lunch_box_review lr on lr.id = r.id " +
                "left join review_image ri " +
                "on ri.review_id = r.id " +
                "group by r.id " +
                "union " +
                "select r.id, date_format(r.created_at, \"%Y-%m-%d\") as created_at, " +
                "(select ri3.url " +
                "from review_image ri3 " +
                "where ri3.id = min(ri.id)) as url, " +
                "'restaurant' as type  " +
                "from review r " +
                "inner join restaurant_review rr on rr.id = r.id " +
                "left join review_image ri  " +
                "on ri.review_id = r.id " +
                "group by r.id) as result " +
                "where year(result.created_at) = :year and month(result.created_at) = :month " +
                "order by result.created_at";

        FIND_ALL_REVIEW_DETAIL_BETWEEN_DATE_QUERY = "SELECT * " +
                "FROM (" +
                "SELECT " +
                "review.id as reviewId, " +
                "review.selected_dishes as reviewTitle, " +
                "restaurant_review.taste as reviewTaste, " +
                "review.description as reviewDescription, " +
                "date_format(review.created_at, \"%Y-%m-%d\") as reviewCreatedAt, " +
                "restaurant.name as location, " +
                "GROUP_CONCAT(review_image.url SEPARATOR :separator) AS images, " +
                "'restaurant' AS reviewType " +
                "FROM review " +
                "LEFT JOIN review_image ON review_image.review_id = review.id " +
                "INNER JOIN restaurant_review on restaurant_review.id = review.id " +
                "INNER JOIN restaurant on restaurant.id = restaurant_review.restaurant_id " +
                "WHERE review.member_id = :memberId " +
                "GROUP BY review.id, review.member_id " +
                "UNION " +
                "SELECT " +
                "review.id as reviewId, " +
                "review.selected_dishes as reviewTitle, " +
                "cafeteria_review.taste as reviewTaste, " +
                "review.description as reviewDescription, " +
                "date_format(review.created_at, \"%Y-%m-%d\") as reviewCreatedAt, " +
                "building.name as location, " +
                "GROUP_CONCAT(review_image.url SEPARATOR :separator) AS images, " +
                "'cafeteria' AS reviewType " +
                "FROM review " +
                "LEFT JOIN review_image ON review_image.review_id = review.id " +
                "INNER JOIN cafeteria_review on cafeteria_review.id = review.id " +
                "INNER JOIN building on building.id = cafeteria_review.building_id " +
                "WHERE review.member_id = :memberId " +
                "GROUP BY review.id, review.member_id " +
                "UNION " +
                "SELECT " +
                "review.id as reviewId, " +
                "review.selected_dishes as reviewTitle, " +
                "NULL as reviewTaste, " +
                "review.description as reviewDescription, " +
                "date_format(review.created_at, \"%Y-%m-%d\") as reviewCreatedAt, " +
                "NULL as location, " +
                "GROUP_CONCAT(review_image.url SEPARATOR :separator) AS images, " +
                "'lunchBox' AS reviewType " +
                "FROM review " +
                "LEFT JOIN review_image ON review_image.review_id = review.id " +
                "INNER JOIN lunch_box_review on lunch_box_review.id = review.id " +
                "WHERE review.member_id = :memberId " +
                "GROUP BY review.id, review.member_id " +
                ") as data " +
                "WHERE data.reviewCreatedAt BETWEEN :startDate AND :endDate " +
                "ORDER BY data.reviewCreatedAt";
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<ReviewResponse.CalendarListDTO> findCalendarListByYearAndMonth(String year, String month) {
        Query query = entityManager.createNativeQuery(FIND_ALL_REVIEWS_BY_YEAR_AND_MONTH_QUERY, "ReviewAllList")
                .setParameter("year", year)
                .setParameter("month", month);

        return query.getResultList();
    }

    public List<Projection.AllReviewDetailDTO> findAllReviewDetailBetween(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        Query query = entityManager.createNativeQuery(FIND_ALL_REVIEW_DETAIL_BETWEEN_DATE_QUERY, "AllReviewDetailListMapping")
                .setParameter("separator", ImageSeparator.IMAGE_SEPARATOR)
                .setParameter("memberId", memberId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);

        return query.getResultList();
    }
}
