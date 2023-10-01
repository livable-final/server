package com.livable.server.restaurant.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Building;
import com.livable.server.entity.Company;
import com.livable.server.entity.Member;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.restaurant.domain.RandomGenerator;
import com.livable.server.restaurant.domain.RestaurantErrorCode;
import com.livable.server.restaurant.dto.RestaurantByMenuProjection;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.dto.RestaurantResponse.RestaurantsByMenuDto;
import com.livable.server.restaurant.repository.BuildingRestaurantMapRepository;
import com.livable.server.restaurant.repository.RestaurantGroupByMenuProjectionRepository;
import com.livable.server.restaurant.repository.RestaurantRepository;
import com.livable.server.review.domain.ReviewErrorCode;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.repository.VisitorRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private final RandomGenerator<Pageable> randomGenerator;

    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;
    private final BuildingRestaurantMapRepository buildingRestaurantMapRepository;
    private final MemberRepository memberRepository;
    private final RestaurantGroupByMenuProjectionRepository restaurantGroupByMenuProjectionRepository;

    public List<RestaurantResponse.NearRestaurantDto> findNearRestaurantByVisitorIdAndRestaurantCategory(
            Long visitorId, RestaurantCategory category
    ) {
        Long buildingId = visitorRepository.findBuildingIdById(visitorId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND));

        Integer nearRestaurantCount =
                buildingRestaurantMapRepository.countBuildingRestaurantMapByBuildingIdAndRestaurant_RestaurantCategory(
                        buildingId,
                        category
                );

        return restaurantRepository.findRestaurantByBuildingIdAndRestaurantCategory(
                buildingId, category, randomGenerator.getRandom(nearRestaurantCount)
        );
    }


    public List<RestaurantResponse.listMenuDTO> findMenuList(Long memberId, Long restaurantId) {
        checkExistMemberById(memberId);

        return restaurantRepository.findMenuList(restaurantId);
    }

    private Member checkExistMemberById(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        return memberOptional.orElseThrow(() -> new GlobalRuntimeException(ReviewErrorCode.MEMBER_NOT_EXIST));
    }

    public List<RestaurantsByMenuDto> findRestaurantByMenuId(Long menuId, Long memberId) {
        List<RestaurantByMenuProjection> restaurantByMenuProjections = restaurantGroupByMenuProjectionRepository.findRestaurantByMenuId(menuId, memberId);

        if (restaurantByMenuProjections.isEmpty()) {
            throw new GlobalRuntimeException(RestaurantErrorCode.NOT_FOUND_RESTAURANT_BY_MENU);
        }

        return getRestaurantsByMenu(restaurantByMenuProjections);
    }

    private List<RestaurantsByMenuDto> getRestaurantsByMenu(
        List<RestaurantByMenuProjection> restaurantByMenuProjections) {

        List<RestaurantsByMenuDto> restaurantsByMenuDtos = new ArrayList<>();

        for (RestaurantByMenuProjection restaurantByMenuProjection : restaurantByMenuProjections) {
            restaurantsByMenuDtos.add(RestaurantsByMenuDto.from(restaurantByMenuProjection));
        }

        return restaurantsByMenuDtos;
    }

    public List<RestaurantResponse.SearchRestaurantsDTO> findRestaurantByKeyword(Long memberId, String keyword) {
        checkExistMemberById(memberId);

        Long buildingId = getBuildingByMember(memberId);

        return restaurantRepository.findRestaurantByKeyword(buildingId, keyword);
    }

    private Long getBuildingByMember(Long memberId) {

        return memberRepository.findBuildingInfoByMemberId(memberId).get().getBuildingId();
    }
}