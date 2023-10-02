package com.livable.server.entity;

import com.livable.server.review.dto.Projection;
import com.livable.server.review.dto.ReviewResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "ReviewAllList",
        classes = @ConstructorResult(
                targetClass = ReviewResponse.CalendarListDTO.class,
                columns = {
                        @ColumnResult(name = "reviewId", type = Long.class),
                        @ColumnResult(name = "type", type = String.class),
                        @ColumnResult(name = "reviewImageUrl", type = String.class),
                        @ColumnResult(name = "reviewDate", type = LocalDate.class)
                }
        )
)
@SqlResultSetMapping(
        name = "AllReviewDetailListMapping",
        classes = @ConstructorResult(
                targetClass = Projection.AllReviewDetailDTO.class,
                columns = {
                        @ColumnResult(name = "reviewId", type = Long.class),
                        @ColumnResult(name = "reviewTitle", type = String.class),
                        @ColumnResult(name = "reviewTaste", type = String.class),
                        @ColumnResult(name = "reviewDescription", type = String.class),
                        @ColumnResult(name = "reviewCreatedAt", type = String.class),
                        @ColumnResult(name = "location", type = String.class),
                        @ColumnResult(name = "images", type = String.class),
                        @ColumnResult(name = "reviewType", type = String.class)
                }
        )
)
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@Entity
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String selectedDishes;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

