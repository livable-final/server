package com.livable.server.visitation.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VisitationRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("VisitationRequest.RegisterParkingDto carNumber 정규식 성공 테스트")
    @CsvSource({"12가1234", "12흫3456", "123핡0000"})
    @ParameterizedTest(name = "[{index}] 차량번호: {0}")
    void carNumberPatternSuccessTest(String carNumber) throws JsonProcessingException {
        String requestDto = "{\n" +
                "            \"carNumber\": \"" + carNumber + "\"\n" +
                "        }";

        VisitationRequest.RegisterParkingDto registerParkingDto =
                objectMapper.readValue(requestDto, VisitationRequest.RegisterParkingDto.class);

        Set<ConstraintViolation<VisitationRequest.RegisterParkingDto>> validate = validator.validate(registerParkingDto);

        assertThat(validate.size()).isEqualTo(0);
    }
}