package com.livable.server.visitation.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.visitation.domain.VisitationValidationMessage;
import com.livable.server.visitation.mock.MockRegisterParkingDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VisitationRequestTest {

    private static Validator validator;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    

    @BeforeAll
    public static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @DisplayName("VisitationRequest.RegisterParkingDto carNumber Pattern 성공 테스트")
    @ValueSource(strings = {"12가1234", "12흫3456", "123핡0000"})
    @ParameterizedTest(name = "[{index}] 차량번호: {0}")
    void carNumberPatternSuccessTest(String carNumber) throws JsonProcessingException {
        VisitationRequest.RegisterParkingDto registerParkingDto = serialize(carNumber);

        Set<ConstraintViolation<VisitationRequest.RegisterParkingDto>> validate = validator.validate(registerParkingDto);

        assertThat(validate.size()).isEqualTo(0);
    }

    @DisplayName("VisitationRequest.RegisterParkingDto carNumber Pattern 실패 테스트")
    @ValueSource(strings = {"123", "124", "가가가각가"})
    @ParameterizedTest(name = "[{index}] 차량번호: {0}")
    void carNumberPatternFailTest(String carNumber) throws JsonProcessingException {
        VisitationRequest.RegisterParkingDto registerParkingDto = serialize(carNumber);

        Set<ConstraintViolation<VisitationRequest.RegisterParkingDto>> validate = validator.validate(registerParkingDto);
        List<String> errorMessages = getErrorMessages(validate);

        assertThat(validate.size()).isEqualTo(1);
        assertThat(errorMessages.contains(VisitationValidationMessage.INVALID_CAR_NUMBER)).isTrue();
    }

    @DisplayName("VisitationRequest.RegisterParkingDto carNumber NotBlank 성공 테스트_1")
    @ValueSource(strings = {"abcde", "124", "가가가각가"})
    @ParameterizedTest(name = "[{index}] 차량번호: {0}")
    void carNumberNotBlankSuccess(String carNumber) throws JsonProcessingException {
        VisitationRequest.RegisterParkingDto registerParkingDto = serialize(carNumber);

        Set<ConstraintViolation<VisitationRequest.RegisterParkingDto>> validate = validator.validate(registerParkingDto);

        List<String> messages = getErrorMessages(validate);

        assertThat(validate.size()).isEqualTo(1);
        assertThat(messages.contains(VisitationValidationMessage.INVALID_CAR_NUMBER)).isTrue();
    }

    @DisplayName("VisitationRequest.RegisterParkingDto carNumber NotBlank 실패 테스트_1")
    @ValueSource(strings = {"", " "})
    @ParameterizedTest(name = "[{index}] 차량번호: {0}")
    void carNumberNotBlankFailTest_1(String carNumber) throws JsonProcessingException {
        VisitationRequest.RegisterParkingDto registerParkingDto = serialize(carNumber);

        Set<ConstraintViolation<VisitationRequest.RegisterParkingDto>> validate = validator.validate(registerParkingDto);

        List<String> errorMessages = getErrorMessages(validate);

        assertThat(validate.size()).isEqualTo(2);
        assertThat(errorMessages.contains(VisitationValidationMessage.NOT_BLANK)).isTrue();
    }

    @DisplayName("VisitationRequest.RegisterParkingDto carNumber NotBlank 실패 테스트_2")
    @NullSource
    @ParameterizedTest
    void carNumberNotBlankFailTest_2(String carNumber) throws JsonProcessingException {
        VisitationRequest.RegisterParkingDto registerParkingDto = serialize(carNumber);

        Set<ConstraintViolation<VisitationRequest.RegisterParkingDto>> validate = validator.validate(registerParkingDto);

        List<String> errorMessages = getErrorMessages(validate);

        assertThat(validate.size()).isEqualTo(1);
        assertThat(errorMessages.contains(VisitationValidationMessage.NOT_BLANK)).isTrue();
    }

    private VisitationRequest.RegisterParkingDto serialize(String carNumber) throws JsonProcessingException {

        if (carNumber == null) {
            return new VisitationRequest.RegisterParkingDto();
        }

        String template = "{\"carNumber\":\"" + carNumber + "\"}";

        return objectMapper.readValue(template, VisitationRequest.RegisterParkingDto.class);
    }

    private static List<String> getErrorMessages(Set<ConstraintViolation<VisitationRequest.RegisterParkingDto>> validate) {
        return validate.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }
}