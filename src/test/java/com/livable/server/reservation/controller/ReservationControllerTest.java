package com.livable.server.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.reservation.dto.ReservationResponse;
import com.livable.server.reservation.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(ReservationController.class)
@AutoConfigureRestDocs
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider tokenProvider;

    @MockBean
    ReservationService reservationService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName(
            "[GET][/api/reservation/places/{commonPlaceId}?date={yyyy-MM-dd}] - 특정 회의실의 사용 가능 시간 응답(예약해둔 시간)"
    )
    @Test
    void findAvailableTimesSuccessTest() throws Exception {
        // given
        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

        List<ReservationResponse.AvailableReservationTimePerDateDto> result = IntStream.range(1, 10)
                .mapToObj(idx -> ReservationResponse.AvailableReservationTimePerDateDto.builder()
                        .date(LocalDate.now().plusDays(idx))
                        .availableTimes(new ArrayList<>(List.of(LocalTime.of(idx + 7, idx % 2 * 30))))
                        .build()
                )
                .collect(Collectors.toList());

        given(reservationService.findAvailableReservationTimes(anyLong(), anyLong(), any(LocalDate.class)))
                .willReturn(result);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/reservation/places/{commonPlaceId}", 1)
                        .param("date", LocalDate.now().toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].date").isString())
                .andExpect(jsonPath("$.data[0].availableTimes").isArray())
                .andExpect(jsonPath("$.data[0].availableTimes[0]").isString())
                .andDo(document(
                                "{class-name}/{method-name}",
                                preprocessRequest(removeHeaders("Authorization"), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("commonPlaceId").description("공용 회의실 식별 값")
                                ),
                                requestParameters(
                                        parameterWithName("date").description("예약 날짜")
                                ),

                                responseFields(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("전체 데이터"),
                                        fieldWithPath("data[].date").type(JsonFieldType.STRING).description("날짜"),
                                        fieldWithPath("data[].availableTimes").type(JsonFieldType.ARRAY).description("날짜별 사용 가능 시간 리스트"),
                                        fieldWithPath("data[].availableTimes[]").type(JsonFieldType.ARRAY).description("날짜별 사용 가능 시간")
                                )
                        )
                );
    }
}