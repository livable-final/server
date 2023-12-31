package com.livable.server.invitation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.invitation.domain.InvitationErrorCode;
import com.livable.server.invitation.domain.InvitationValidationMessage;
import com.livable.server.invitation.dto.InvitationRequest;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.service.InvitationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(InvitationController.class)
class InvitationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private InvitationService invitationService;

    @DisplayName("[성공] 예약 가능한 리스트 목록 - 정상 응답")
    @Test
    void getAvailablePlacesSuccess_01() throws Exception {
        // Given
        Long memberId = 1L;
        given(invitationService.getAvailablePlaces(memberId))
                .willReturn(new ResponseEntity<>(
                        ApiResponse.Success.of(
                                InvitationResponse.AvailablePlacesDTO.builder()
                                        .offices(createOfficeDTOList())
                                        .commonPlaces(createCommonPlaceDTOList())
                                        .build()
                        ),
                        HttpStatus.OK
                ));

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                get("/api/invitation/places/available")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data['offices']").isArray())
                .andExpect(jsonPath("$.data['offices'][0]['officeName']").value("사무실 A"))
                .andExpect(jsonPath("$.data['commonPlaces']").isArray())
                .andExpect(jsonPath("$.data['commonPlaces'][0]['commonPlaceId']").value(1));
    }

    private List<InvitationResponse.OfficeDTO> createOfficeDTOList() {
        return List.of(
                InvitationResponse.OfficeDTO.builder().officeName("사무실 A").build(),
                InvitationResponse.OfficeDTO.builder().officeName("사무실 B").build(),
                InvitationResponse.OfficeDTO.builder().officeName("사무실 C").build()
        );
    }

    private List<InvitationResponse.CommonPlaceDTO> createCommonPlaceDTOList() {
        return List.of(
                InvitationResponse.CommonPlaceDTO.builder().commonPlaceId(1L).build(),
                InvitationResponse.CommonPlaceDTO.builder().commonPlaceId(2L).build(),
                InvitationResponse.CommonPlaceDTO.builder().commonPlaceId(3L).build()
        );
    }

    @DisplayName("[실패] 예약 가능한 리스트 목록 - GlobalException 발생, 400")
    @Test
    void getAvailablePlacesFail_01() throws Exception {
        // Given
        given(invitationService.getAvailablePlaces(anyLong()))
                .willThrow(new GlobalRuntimeException(InvitationErrorCode.MEMBER_NOT_EXIST));

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(get("/api/invitation/places/available")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationErrorCode.MEMBER_NOT_EXIST.getMessage()));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (시작 날짜가 오늘 보다 과거일 경우)")
    @Test
    void createInvitationFail_01() throws Exception {
        // Given
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("interview")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(10, 0, 0)))
                .endDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 30, 0)))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(List.of(
                        InvitationRequest.VisitorCreateDTO.builder()
                                .name("홍길동")
                                .contact("01012341234")
                                .build()
                ))
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                        post("/api/invitation")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.REQUIRED_FUTURE_DATE));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (방문자가 한 명도 없는 경우)")
    @Test
    void createInvitationFail_02() throws Exception {
        // Given
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("interview")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 0, 0)))
                .endDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 30, 0)))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(List.of())
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                post("/api/invitation")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(InvitationValidationMessage.REQUIRED_VISITOR_COUNT));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (방문자가 31명인 경우)")
    @Test
    void createInvitationFail_03() throws Exception {
        // Given
        List<InvitationRequest.VisitorCreateDTO> visitors = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            visitors.add(InvitationRequest.VisitorCreateDTO.builder().name("홍길동").contact("01012341234").build());
        }

        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("interview")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 0, 0)))
                .endDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 30, 0)))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(visitors)
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                        post("/api/invitation")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.REQUIRED_VISITOR_COUNT));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (방문자 이름이 영어인 경우)")
    @Test
    void createInvitationFail_04() throws Exception {
        // Given
        List<InvitationRequest.VisitorCreateDTO> visitors = new ArrayList<>();
        visitors.add(InvitationRequest.VisitorCreateDTO.builder().name("testName").contact("01012341234").build());

        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("interview")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 0, 0)))
                .endDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 30, 0)))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(visitors)
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                        post("/api/invitation")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.VISITOR_NAME_FORMAT));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (방문자 이름이 한 글자인 경우)")
    @Test
    void createInvitationFail_05() throws Exception {
        // Given
        List<InvitationRequest.VisitorCreateDTO> visitors = new ArrayList<>();
        visitors.add(InvitationRequest.VisitorCreateDTO.builder().name("김").contact("01012341234").build());

        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("interview")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 0, 0)))
                .endDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 30, 0)))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(visitors)
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                        post("/api/invitation")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.VISITOR_NAME_MIN_SIZE));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (방문자 전화번호에 숫자 이외의 문자가 섞인 경우)")
    @Test
    void createInvitationFail_06() throws Exception {
        // Given
        List<InvitationRequest.VisitorCreateDTO> visitors = new ArrayList<>();
        visitors.add(InvitationRequest.VisitorCreateDTO.builder().name("홍길동").contact("01012341234as").build());

        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("interview")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 0, 0)))
                .endDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 30, 0)))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(visitors)
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                        post("/api/invitation")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.VISITOR_CONTACT_FORMAT));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (방문자 전화번호 길이가 9자인 경우)")
    @Test
    void createInvitationFail_07() throws Exception {
        // Given
        List<InvitationRequest.VisitorCreateDTO> visitors = new ArrayList<>();
        visitors.add(InvitationRequest.VisitorCreateDTO.builder().name("홍길동").contact("010123412").build());

        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("interview")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 0, 0)))
                .endDate(LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(10, 30, 0)))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(visitors)
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                        post("/api/invitation")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.VISITOR_CONTACT_MIN_SIZE));
    }

    @DisplayName("[실패] 초대장 상세 조회 - 초대장 주인과 요청한 사람이 다른 경우")
    @Test
    void getInvitationFail_01() throws Exception {
        // Given
        Long invitationId = 1L;
        given(invitationService.getInvitation(anyLong(), anyLong()))
                .willThrow(new GlobalRuntimeException(InvitationErrorCode.INVALID_INVITATION_OWNER));

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                get("/api/invitation/{invitationId}", invitationId)
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(InvitationErrorCode.INVALID_INVITATION_OWNER.getMessage()));
    }

    @DisplayName("[성공] 초대장 상세 조회")
    @Test
    void getInvitationSuccess_01() throws Exception {
        // Given
        Long invitationId = 1L;
        given(invitationService.getInvitation(anyLong(), anyLong()))
                .willReturn(ApiResponse.success(InvitationResponse.DetailDTO.builder().build(), HttpStatus.OK));

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When & Then
        mockMvc.perform(
                get("/api/invitation/{invitationId}", invitationId)
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("[실패] 초대장 수정 - 시작 날짜, 종료 날짜가 요청 날짜보다 과거인 경우")
    @Test
    void updateInvitationFail_01() throws Exception {
        // Given
        LocalDateTime requestDate = LocalDateTime.now();
        Long invitationId = 1L;
        Long commonPlaceId = 1L;
        InvitationRequest.UpdateDTO dto = InvitationRequest.UpdateDTO.builder()
                .commonPlaceId(commonPlaceId)
                .description("엘리베이터 타고 오른쪽으로 오면 바로 있습니다.")
                .startDate(requestDate.minusDays(1L))
                .endDate(requestDate.minusDays(1L))
                .visitors(List.of())
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When
        ResultActions resultActions = mockMvc.perform(
                patch("/api/invitation/{invitationId}", invitationId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.REQUIRED_FUTURE_DATE));
    }

    @DisplayName("[실패] 초대장 수정 - 방문자 리스트가 널인 경우")
    @Test
    void updateInvitationFail_02() throws Exception {
        // Given
        LocalDateTime requestDate = LocalDateTime.now();
        Long invitationId = 1L;
        Long commonPlaceId = 1L;
        InvitationRequest.UpdateDTO dto = InvitationRequest.UpdateDTO.builder()
                .commonPlaceId(commonPlaceId)
                .description("엘리베이터 타고 오른쪽으로 오면 바로 있습니다.")
                .startDate(requestDate.plusDays(1L))
                .endDate(requestDate.plusDays(1L))
                .visitors(null)
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When
        ResultActions resultActions = mockMvc.perform(
                patch("/api/invitation/{invitationId}", invitationId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.NOT_NULL));
    }

    @DisplayName("[실패] 초대장 수정 - 방문자 데이터가 널인 경우")
    @Test
    void updateInvitationFail_03() throws Exception {
        // Given
        LocalDateTime requestDate = LocalDateTime.now();
        Long invitationId = 1L;
        Long commonPlaceId = 1L;
        InvitationRequest.UpdateDTO dto = InvitationRequest.UpdateDTO.builder()
                .commonPlaceId(commonPlaceId)
                .description("엘리베이터 타고 오른쪽으로 오면 바로 있습니다.")
                .startDate(requestDate.plusDays(1L))
                .endDate(requestDate.plusDays(1L))
                .visitors(List.of(
                        InvitationRequest.VisitorForUpdateDTO.builder()
                                .name(null)
                                .contact(null)
                                .build()
                ))
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When
        ResultActions resultActions = mockMvc.perform(
                patch("/api/invitation/{invitationId}", invitationId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.NOT_NULL));
    }

    @DisplayName("[성공] 초대장 수정")
    @Test
    void updateInvitationSuccess_01() throws Exception {
        // Given
        LocalDateTime requestDate = LocalDateTime.now();
        Long invitationId = 1L;
        Long commonPlaceId = 1L;
        InvitationRequest.UpdateDTO dto = InvitationRequest.UpdateDTO.builder()
                .commonPlaceId(commonPlaceId)
                .description("엘리베이터 타고 오른쪽으로 오면 바로 있습니다.")
                .startDate(requestDate.plusDays(1L))
                .endDate(requestDate.plusDays(1L))
                .visitors(List.of(
                        InvitationRequest.VisitorForUpdateDTO.builder()
                                .name("홍프링")
                                .contact("01012341234")
                                .build()
                ))
                .build();

        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 1000000));

        // When
        ResultActions resultActions = mockMvc.perform(
                patch("/api/invitation/{invitationId}", invitationId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)));

        // TODO: ApiResponse.success() WildCard 리펙토링 후 Mocking 코드 추가 -> 현재는 Stubbing 안돼서 기본값 반환됨.

        // Then
        resultActions.andExpect(status().isOk());

        verify(invitationService, times(1))
                .updateInvitation(anyLong(), any(InvitationRequest.UpdateDTO.class), anyLong());
    }
}