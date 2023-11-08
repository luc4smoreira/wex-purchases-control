package mirand.lucas.wexpurchasescontrol.model.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mirand.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangeRateDataDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangeRateResponseDTO;
import mirand.lucas.wexpurchasescontrol.model.services.imp.TreasureGovExchangeRateServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TreasureGovExchangeRateServiceImpTest {

    @MockBean
    private RestTemplateBuilder restTemplateBuilderMock;

    private TreasureGovExchangeRateServiceImp service;



    @Test
    public void testAcceptableInterval() {

        when(restTemplateBuilderMock.build()).thenReturn(mock(RestTemplate.class));
        service = new TreasureGovExchangeRateServiceImp(restTemplateBuilderMock);

        DateIntervalDTO dateIntervalDTO = service.generateAcceptableInterval(LocalDate.now());


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<DateIntervalDTO>> violations = validator.validate(dateIntervalDTO);

        Assertions.assertTrue(violations.isEmpty());


        LocalDate now = LocalDate.now();
        LocalDate past6months = now.minusMonths(6);


        Assertions.assertEquals(past6months.toString(), dateIntervalDTO.getStart().toString());
        Assertions.assertEquals(now.toString(), dateIntervalDTO.getEnd().toString());
    }

    @Test
    public void testFutureInterval() {

        when(restTemplateBuilderMock.build()).thenReturn(mock(RestTemplate.class));
        service = new TreasureGovExchangeRateServiceImp(restTemplateBuilderMock);


        DateIntervalDTO dateIntervalDTO = service.generateAcceptableInterval(LocalDate.now().plusDays(1));


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<DateIntervalDTO>> violations = validator.validate(dateIntervalDTO);

        Assertions.assertTrue(!violations.isEmpty());

    }

    @Test
    public void testApiCall() {

//        BigDecimal expected = new BigDecimal(1.42);
//        // Configurar o mock do RestTemplate para retornar um resultado esperado
//        ExchangeRateResponseDTO exchangeRateResponseDTO = new ExchangeRateResponseDTO();
//
//        ExchangeRateDataDTO exchangeRateDataDTO = new ExchangeRateDataDTO();
//        exchangeRateDataDTO.setExchangeRate(expected);
//
//        List<ExchangeRateDataDTO> data = new ArrayList<>();
//        data.add(exchangeRateDataDTO);
//
//        exchangeRateResponseDTO.setData(data);
//
//        RestTemplate mockRestTemplate = mock(RestTemplate.class);
//        when(mockRestTemplate.getForObject(any(String.class), any())
//        ).thenReturn(exchangeRateResponseDTO);
//
//
//        when(restTemplateBuilderMock.build()).thenReturn(mockRestTemplate);
//
//        service = new TreasureGovExchangeRateServiceImp(restTemplateBuilderMock);
//
//
//        DateIntervalDTO dateIntervalDTO = service.generateAcceptableInterval(LocalDate.now());
//
//        BigDecimal result = service.getNewestExchangeRateInInterval("Brazil", "Real", dateIntervalDTO);
//        Assertions.assertEquals(0, expected.compareTo(result));

    }


}