package com.livable.server.reservation.repository;

import com.livable.server.core.config.QueryDslConfig;
import com.livable.server.entity.*;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.reservation.dto.AvailableReservationTimeProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class ReservationRepositoryTest {

    public static final LocalDate START_DATE = LocalDate.now();
    public static final LocalTime START_TIME = LocalTime.of(1, 10);
    public static final LocalTime END_TIME = LocalTime.of(1, 20);
    public static final LocalDate END_DATE = LocalDate.now();

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void dateInit() {
        Building building = Building.builder()
                .name("63빌딩")
                .scale("지하 3층, 지상 63층")
                .representativeImageUrl("./thumbnailImage.jpg")
                .endTime(LocalTime.of(10, 30))
                .startTime(LocalTime.of(18, 30))
                .parkingCostInformation("10분당 1000원")
                .longitude("10.10.10.10")
                .latitude("123.123.123")
                .hasCafeteria(false)
                .address("서울시 강남구 서초대로 61길 7, 392")
                .subwayStation("석촌역")
                .build();

        entityManager.persist(building);

        Company company = Company.builder()
                .name("패스트캠퍼스")
                .building(building)
                .build();

        entityManager.persist(company);

        Member member = Member.builder()
                .company(company)
                .contact("01012345678")
                .name("김훈섭")
                .email("test@naver.com")
                .employeeNumber("9q0mavfdmmpoaskp")
                .profileImageUrl("./profileImageUrl")
                .password("1234")
                .role(Role.USER)
                .build();

        entityManager.persist(member);

        Invitation invitation = Invitation.builder()
                .member(member)
                .endDate(END_DATE)
                .startDate(START_DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .description("알아서 와")
                .purpose("INTERVIEW")
                .officeName("패스트캠퍼스 사무실")
                .build();

        entityManager.persist(invitation);

        List<CommonPlace> commonPlaceList = IntStream.range(0, 2)
                .mapToObj(
                        idx -> CommonPlace.builder()
                                .name("commonPlace" + idx)
                                .floor("floor" + idx)
                                .roomNumber("roomNumber" + idx)
                                .building(building)
                                .build()
                )
                .collect(Collectors.toList());
        commonPlaceList.forEach(entityManager::persist);

        List<Reservation> reservationList = IntStream.range(1, 10)
                .mapToObj(
                        idx -> Reservation.builder()
                                .date(LocalDate.now())
                                .time(LocalTime.of(10, 0, 0).plusMinutes(idx * 30L))
                                .commonPlace(commonPlaceList.get(idx % 2))
                                .company(company)
                                .build()
                )
                .collect(Collectors.toList());

        reservationList.forEach(entityManager::persist);

        List<InvitationReservationMap> invitationReservationMapList = IntStream.range(0, 3)
                .mapToObj(idx -> {
                    return InvitationReservationMap.builder()
                            .reservation(reservationList.get(idx))
                            .invitation(invitation)
                            .build();
                })
                .collect(Collectors.toList());

        invitationReservationMapList.forEach(entityManager::persist);
    }

    @DisplayName("ReservationRepository.findNotUsedReservationTime 쿼리 성공 테스트")
    @Test
    void test() {

        List<AvailableReservationTimeProjection> expectedResult = IntStream.range(4, 10)
                .filter(idx -> idx % 2 == 0)
                .mapToObj(idx ->
                        new AvailableReservationTimeProjection(
                        LocalDate.now(), LocalTime.of(10, 0, 0).plusMinutes(30L * idx)
                        )
                )
                .collect(Collectors.toList());


        Member member = entityManager.createQuery("select m from Member m", Member.class)
                .getResultList()
                .get(0);

        CommonPlace commonPlace = entityManager.createQuery("select cp from CommonPlace cp", CommonPlace.class)
                .getResultList()
                .get(0);
        List<AvailableReservationTimeProjection> notUsedReservationTime =
                reservationRepository.findNotUsedReservationTime(
                        member.getCompany().getId(), commonPlace.getId(), LocalDate.now()
                );

        assertThat(expectedResult).usingRecursiveComparison().isEqualTo(notUsedReservationTime);
    }


}