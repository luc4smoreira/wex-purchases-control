package mirand.lucas.wexpurchasescontrol.model.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mirand.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import mirand.lucas.wexpurchasescontrol.model.services.imp.TreasureGovExchangeRateServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TreasureGovExchangeRateServiceImpTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private TreasureGovExchangeRateServiceImp service;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilderMock.build()).thenReturn(mock(RestTemplate.class));
        service = new TreasureGovExchangeRateServiceImp(restTemplateBuilderMock);
    }


    @Test
    public void testAcceptableInterval() {
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
        DateIntervalDTO dateIntervalDTO = service.generateAcceptableInterval(LocalDate.now().plusDays(1));


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<DateIntervalDTO>> violations = validator.validate(dateIntervalDTO);

        Assertions.assertTrue(!violations.isEmpty());

    }


}
