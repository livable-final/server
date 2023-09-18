package com.livable.server.invitation.controller;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse;
import com.livable.server.invitation.domain.InvitationErrorCode;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.service.InvitationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvitationController.class)
class InvitationControllerTest {

    @Autowired
    MockMvc mockMvc;

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
        given(invitationService.getAvailablePlaces(ArgumentMatchers.anyLong()))
                .willThrow(new GlobalRuntimeException(InvitationErrorCode.MEMBER_NOT_EXIST));

        // When & Then
        mockMvc.perform(get("/api/invitation/places/available"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(InvitationErrorCode.MEMBER_NOT_EXIST.getMessage()));
    }
}