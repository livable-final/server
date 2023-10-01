package com.livable.server.review.repository;

import com.livable.server.review.dto.ReviewResponse;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ReviewProjectionRepository {
    private static final String FIND_ALL_REVIEWS_BY_YEAR_AND_MONTH_QUERY;

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
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<ReviewResponse.CalendarListDTO> findCalendarListByYearAndMonth(String year, String month) {
        Query query = entityManager.createNativeQuery(FIND_ALL_REVIEWS_BY_YEAR_AND_MONTH_QUERY, "ReviewAllList")
                .setParameter("year", year)
                .setParameter("month", month);

        return query.getResultList();
    }
}
