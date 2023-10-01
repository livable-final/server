package com.livable.server.review.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.S3Uploader;
import com.livable.server.entity.*;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.restaurant.repository.RestaurantRepository;
import com.livable.server.review.domain.ReviewErrorCode;
import com.livable.server.review.dto.ReviewRequest;
import com.livable.server.review.dto.ReviewResponse;
import com.livable.server.review.repository.ReviewImageRepository;
import com.livable.server.review.repository.ReviewProjectionRepository;
import com.livable.server.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.livable.server.review.domain.ReviewSelectType.*;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewProjectionRepository reviewProjectionRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createLunchBoxReview(ReviewRequest.LunchBoxCreateDTO lunchBoxCreateDTO, Long memberId, List<MultipartFile> files) throws IOException {
        Member member = findMemberById(memberId);
        Review review = lunchBoxCreateDTO.toEntity(member, LUNCH_BOX.getMessage());
        reviewRepository.save(review);

        List<String> images = s3Uploader.saveFile(files);

        if (!images.isEmpty()) {
            // add point
            // 하루에 리뷰 한개만 인지 체크
            // 포인트 10점 넣기

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
        reviewRepository.save(review);

        List<String> images = s3Uploader.saveFile(files);

        if (!images.isEmpty()) {
            // add point

            // register image
            List<ReviewImage> reviewImages = saveImageFiles(review, images);

            reviewImageRepository.saveAll(reviewImages);
        }
    }

    @Transactional
    public void createRestaurantReview(ReviewRequest.RestaurantCreateDTO restaurantCreateDTO, Long memberId, List<MultipartFile> files) throws IOException {
        String selectedDishes = "";
        StringBuffer sb = new StringBuffer();

        List<Menu> menu = restaurantCreateDTO.getMenus();
        Long restaurantId = restaurantCreateDTO.getRestaurantId();
        List<String> customMenu = restaurantCreateDTO.getCustomMenus();

        Member member = findMemberById(memberId);
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (menu.isEmpty() && customMenu.isEmpty()) {
            throw new GlobalRuntimeException(ReviewErrorCode.MENUS_NOT_CHOICE);
        }


        menu.forEach(el -> {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(el.getName());
        });

        customMenu.forEach(el -> {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(el);
        });

        selectedDishes = sb.substring(0, sb.length());

        Review review = restaurantCreateDTO.toEntity(member, restaurant, selectedDishes);
        reviewRepository.save(review);


        List<String> images = s3Uploader.saveFile(files);
        List<ReviewImage> reviewImages = saveImageFiles(review, images);

        reviewImageRepository.saveAll(reviewImages);
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
}