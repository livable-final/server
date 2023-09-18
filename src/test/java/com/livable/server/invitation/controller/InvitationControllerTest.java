package com.livable.server.invitation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvitationController.class)
class InvitationControllerTest {

    @Autowired
    MockMvc mockMvc;

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

        // When & Then
        mockMvc.perform(get("/api/invitation/places/available"))
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

        // When & Then
        mockMvc.perform(get("/api/invitation/places/available"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationErrorCode.MEMBER_NOT_EXIST.getMessage()));
    }

    @DisplayName("[실패] 초대장 저장 - 유효성 검사 실패 (시작 날짜가 오늘 보다 과거일 경우)")
    @Test
    void createInvitationFail_01() throws Exception {
        // Given
        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(2023, 9, 18, 10, 0, 0))
                .endDate(LocalDateTime.of(2030, 10, 30, 10, 30, 0))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(List.of(
                        InvitationRequest.VisitorCreateDTO.builder()
                                .name("홍길동")
                                .contact("01012341234")
                                .build()
                ))
                .build();

        // When & Then
        mockMvc.perform(
                post("/api/invitation")
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
                .purpose("면접")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(2030, 10, 30, 10, 0, 0))
                .endDate(LocalDateTime.of(2030, 10, 30, 10, 30, 0))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(List.of())
                .build();

        // When & Then
        mockMvc.perform(
                post("/api/invitation")
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
            visitors.add(InvitationRequest.VisitorCreateDTO.builder().build());
        }

        InvitationRequest.CreateDTO dto = InvitationRequest.CreateDTO.builder()
                .purpose("면접")
                .officeName("공용 라운지")
                .startDate(LocalDateTime.of(2030, 10, 30, 10, 0, 0))
                .endDate(LocalDateTime.of(2030, 10, 30, 10, 30, 0))
                .description("엘리베이터 앞에 있어요.")
                .commonPlaceId(1L)
                .visitors(visitors)
                .build();

        // When & Then
        mockMvc.perform(
                        post("/api/invitation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationValidationMessage.REQUIRED_VISITOR_COUNT));
    }
}