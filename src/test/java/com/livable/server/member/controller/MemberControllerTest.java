package com.livable.server.member.controller;

import com.livable.server.member.dto.MemberResponse;
import com.livable.server.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Nested
    @DisplayName("마이페이지 컨트롤러 단위 테스트")
    class MyRestaurantReview {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/members";

            MemberResponse.MyPageDTO mockDTO = MemberResponse.MyPageDTO.builder()
                    .memberName("TestName")
                    .companyName("TestCompany")
                    .pointValance(200)
                    .build();

            Mockito.when(memberService.getMyPageData(ArgumentMatchers.anyLong()))
                    .thenReturn(mockDTO);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
        }
    }
}