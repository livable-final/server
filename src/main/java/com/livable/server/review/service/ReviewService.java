package com.livable.server.review.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.ImageSeparator;
import com.livable.server.core.util.S3Uploader;
import com.livable.server.entity.*;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.menu.repository.MenuRepository;
import com.livable.server.point.repository.PointLogRepository;
import com.livable.server.point.domain.DateFactory;
import com.livable.server.point.domain.DateRange;
import com.livable.server.restaurant.repository.RestaurantRepository;
import com.livable.server.review.domain.PointReview;
import com.livable.server.review.domain.ReviewErrorCode;
import com.livable.server.review.dto.MenuRequest;
import com.livable.server.review.dto.Projection;
import com.livable.server.review.dto.ReviewRequest;
import com.livable.server.review.dto.ReviewResponse;
import com.livable.server.review.repository.ReviewImageRepository;
import com.livable.server.review.repository.ReviewMenuMapRepository;
import com.livable.server.review.repository.ReviewProjectionRepository;
import com.livable.server.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.livable.server.review.domain.ReviewSelectType.*;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PointLogRepository pointLogRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewMenuMapRepository reviewMenuMapRepository;
    private final ReviewProjectionRepository reviewProjectionRepository;
    private final S3Uploader s3Uploader;
    private final DateFactory dateFactory;
    private final ImageSeparator imageSeparator;

    @Transactional
    public void createLunchBoxReview(ReviewRequest.LunchBoxCreateDTO lunchBoxCreateDTO, Long memberId, List<MultipartFile> files) throws IOException {
        Member member = findMemberById(memberId);
        Review review = lunchBoxCreateDTO.toEntity(member, LUNCH_BOX.getMessage());
        Long reviewCount = reviewRepository.findBymemberIdAndDate(memberId);

        if (reviewCount == 0) {
            reviewRepository.save(review);
        } else {
            throw new GlobalRuntimeException(ReviewErrorCode.ALREADY_HAVE_A_REVIEW);
        }

        List<String> images = s3Uploader.saveFile(files);

        if (!images.isEmpty()) {
            Point point = pointLogRepository.findByMemberId(memberId);

            // add point
            paidPoints(point, PointReview.LUNCHBOX_POINT, review);

            // register image
            List<ReviewImage> reviewImages = saveImageFiles(review, images);

            reviewImageRepository.saveAll(reviewImages);
        }
    }

    @Transactional
    public void createCafeteriaReview(ReviewRequest.CafeteriaCreateDTO cafeteriaCreateDTO, Long memberId, List<MultipartFile> files) throws IOException {
        Member member = findMemberById(memberId);
        Building building = getBuildingByMember(member);
        Review review = cafeteriaCreateDTO.toEntity(member, building, CAFETERIA.getMessage());
        Long reviewCount = reviewRepository.findBymemberIdAndDate(memberId);

        if (reviewCount == 0) {
            reviewRepository.save(review);
        } else {
            throw new GlobalRuntimeException(ReviewErrorCode.ALREADY_HAVE_A_REVIEW);
        }

        List<String> images = s3Uploader.saveFile(files);

        if (!images.isEmpty()) {
            Point point = pointLogRepository.findByMemberId(memberId);

            // add point
            paidPoints(point, PointReview.CAFETERIA_POINT, review);

            // register image
            List<ReviewImage> reviewImages = saveImageFiles(review, images);

            reviewImageRepository.saveAll(reviewImages);
        }
    }

    @Transactional
    public void createRestaurantReview(ReviewRequest.RestaurantCreateDTO restaurantCreateDTO, Long memberId, List<MultipartFile> files) throws IOException {
        String selectedDishes = "";
        StringBuffer sb = new StringBuffer();
        List<Long> menuList = new ArrayList<>();
        List<ReviewMenuMap> reviewMenuMapList =  new ArrayList<>();

        // request menu
        List<MenuRequest> menu = restaurantCreateDTO.getMenus();
        Long restaurantId = restaurantCreateDTO.getRestaurantId();

        Member member = findMemberById(memberId);
        Restaurant restaurant = findRestaurantById(restaurantId);

        Long reviewCount = reviewRepository.findBymemberIdAndDate(memberId);

        // menu valid
        if (menu.isEmpty()) {

            throw new GlobalRuntimeException(ReviewErrorCode.MENUS_NOT_CHOICE);
        }

        // menu 리스트 순회
        menu.forEach(el -> {
            if (el.getMenuId() > 0) {
                menuList.add(el.getMenuId());
            }

            // selected dishes 용
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(el.getMenuName());
        });

        selectedDishes = sb.substring(0, sb.length());
        Review review = restaurantCreateDTO.toEntity(member, restaurant, selectedDishes);

        List<Menu> menus = menuRepository.findAllMenuByMenuId(menuList);

        menus.forEach(el -> {
            reviewMenuMapList.add(ReviewMenuMap.builder()
                    .menu(el)
                    .review(review)
                    .build());
        });

        if (reviewCount == 0) {
            reviewRepository.save(review);
        } else {
            throw new GlobalRuntimeException(ReviewErrorCode.ALREADY_HAVE_A_REVIEW);
        }

        reviewMenuMapRepository.saveAll(reviewMenuMapList);

        List<String> images = s3Uploader.saveFile(files);

        if (!images.isEmpty()) {
            Point point = pointLogRepository.findByMemberId(memberId);

            // add point
            paidPoints(point, PointReview.RESTAURANT_POINT, review);

            List<ReviewImage> reviewImages = saveImageFiles(review, images);
            reviewImageRepository.saveAll(reviewImages);
        }
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);

        return restaurantOptional.orElseThrow(() -> new GlobalRuntimeException(ReviewErrorCode.RESTAURANT_NOT_EXITST));
    }

    private Member findMemberById(Long memberid) {
        Optional<Member> memberOptional = memberRepository.findById(memberid);

        return memberOptional.orElseThrow(() -> new GlobalRuntimeException(ReviewErrorCode.MEMBER_NOT_EXIST));
    }

    private Building getBuildingByMember(Member member) {
        Company company = checkExistMemberById(member.getId()).getCompany();

        return company.getBuilding();
    }

    private Member checkExistMemberById(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        return memberOptional.orElseThrow(() -> new GlobalRuntimeException(ReviewErrorCode.MEMBER_NOT_EXIST));
    }


    private List<ReviewImage> saveImageFiles(Review review, List<String> images) {
        List<ReviewImage> reviewImages = images.stream().map(image ->
            ReviewImage.builder()
                .review(review)
                .url(image)
                .build()
        ).collect(Collectors.toList());

        return reviewImages;
    }

    public List<ReviewResponse.CalendarListDTO> findCalendarList(Long memberId, String year, String month) {

        checkExistMemberById(memberId);

        return reviewProjectionRepository.findCalendarListByYearAndMonth(year, month);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void paidPoints(Point point, PointReview pointReview, Review review) {

        point.plusPoint(pointReview.getAmount());

        PointLog pointLog = PointLog.builder()
                .point(point)
                .review(review)
                .code(pointReview.getPointCode())
                .amount(pointReview.getAmount())
                .build();

        pointLogRepository.save(pointLog);
    }
  
    @Transactional(readOnly = true)
    public List<ReviewResponse.DetailListDTO> findAllReviewDetailList(Long memberId, Integer year, Integer month) {

        LocalDateTime requestTime = LocalDateTime.of(year, month, 1, 0, 0, 0);

        DateRange requestDateRange = dateFactory.getMonthRangeOf(requestTime);
        List<Projection.AllReviewDetailDTO> allReviewDetailDTOS = reviewProjectionRepository.findAllReviewDetailBetween(
                memberId, requestDateRange.getStartDate(), requestDateRange.getEndDate());

        return allReviewDetailDTOS.stream()
                .map(detailDTO -> ReviewResponse.DetailListDTO.valueOf(detailDTO, imageSeparator))
                .collect(Collectors.toList());
    }
}