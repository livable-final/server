package com.livable.server.visitation.repository;

import com.livable.server.core.config.QueryDslConfig;
import com.livable.server.entity.*;
import com.livable.server.visitation.dto.VisitationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(QueryDslConfig.class)
class VisitorRepositoryTest {

    public static final LocalDate START_DATE = LocalDate.now();
    public static final LocalTime START_TIME = LocalTime.of(1, 10);
    public static final LocalTime END_TIME = LocalTime.of(1, 20);
    public static final LocalDate END_DATE = LocalDate.now();

    @Autowired
    VisitorRepository visitorRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void dataInit() {
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

        Visitor visitor = Visitor.builder()
                .invitation(invitation)
                .name("최태윤")
                .contact("01034567811")
                .build();

        entityManager.persist(visitor);
    }

    @DisplayName("VisitorRepository.findVisitationDetailInformationById 쿼리 성공 테스트")
    @Test
    void findVisitationDetailInformationByIdSuccessTest() {
        Visitor visitor = visitorRepository.findAll().get(0);
        Invitation invitation = visitor.getInvitation();
        Member member = invitation.getMember();
        Company company = member.getCompany();
        Building building = company.getBuilding();

        VisitationResponse.DetailInformationDto detailInformationDto =
                visitorRepository.findVisitationDetailInformationById(visitor.getId()).get();

        assertAll(
                () -> assertThat(detailInformationDto.getInvitationStartDate()).isEqualTo(visitor.getInvitation().getStartDate()),
                () -> assertThat(detailInformationDto.getInvitationEndDate()).isEqualTo(visitor.getInvitation().getEndDate()),
                () -> assertThat(detailInformationDto.getInvitationStartTime()).isEqualTo(visitor.getInvitation().getStartTime()),
                () -> assertThat(detailInformationDto.getInvitationEndTime()).isEqualTo(visitor.getInvitation().getEndTime()),
                () -> assertThat(detailInformationDto.getHostName()).isEqualTo(member.getName()),
                () -> assertThat(detailInformationDto.getHostContact()).isEqualTo(member.getContact()),
                () -> assertThat(detailInformationDto.getBuildingAddress()).isEqualTo(building.getAddress()),
                () -> assertThat(detailInformationDto.getBuildingName()).isEqualTo(building.getName()),
                () -> assertThat(detailInformationDto.getBuildingParkingCostInformation()).isEqualTo(building.getParkingCostInformation()),
                () -> assertThat(detailInformationDto.getBuildingRepresentativeImageUrl()).isEqualTo(building.getRepresentativeImageUrl())
        );
    }

    @DisplayName("VisitorRepository.findBuildingIdById 쿼리 테스트_1")
    @Test
    void findBuildingIdByIdSuccessTest_1() {
        Visitor visitor = visitorRepository.findAll().get(0);
        Invitation invitation = visitor.getInvitation();
        Member member = invitation.getMember();
        Company company = member.getCompany();
        Building building = company.getBuilding();

        Long buildingIdById = visitorRepository.findBuildingIdById(visitor.getId()).get();

        assertThat(buildingIdById).isEqualTo(building.getId());
    }

    @DisplayName("VisitorRepository.findBuildingIdById 쿼리 테스트_2")
    @Test
    void findBuildingIdByIdSuccessTest_2() {
        Visitor visitor = visitorRepository.findAll().get(0);
        Invitation invitation = visitor.getInvitation();
        Member member = invitation.getMember();
        Company company = member.getCompany();
        Building building = company.getBuilding();

        Optional<Long> buildingIdById = visitorRepository.findBuildingIdById(visitor.getId() + 1);

        assertThat(buildingIdById).isEqualTo(Optional.empty());
    }
}