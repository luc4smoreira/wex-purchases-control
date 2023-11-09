package mirand.lucas.wexpurchasescontrol.model.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mirand.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangeRateDataDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangeRateResponseDTO;
import mirand.lucas.wexpurchasescontrol.model.services.CurrencyExchangeRateService;
import mirand.lucas.wexpurchasescontrol.model.services.imp.TreasureGovExchangeRateServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TreasureGovExchangeRateServiceImpTest {

    private static final String COUNTRY_BRAZIL = "Brazil";
    private static final String CURRENCEY_REAL = "Real";
    @Mock
    private RestTemplate restTemplate;

    private CurrencyExchangeRateService service;


    @BeforeEach
    void setUp() {
        service = new TreasureGovExchangeRateServiceImp(restTemplate);
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

        Assertions.assertFalse(violations.isEmpty());

    }

    @Test
    public void testGetNewestExchangeRateInIntervalReturnSuccessfull() {

        BigDecimal expected = new BigDecimal("1.42");
        // Configurar o mock do RestTemplate para retornar um resultado esperado
        ExchangeRateResponseDTO exchangeRateResponseDTO = new ExchangeRateResponseDTO();

        ExchangeRateDataDTO exchangeRateDataDTO = new ExchangeRateDataDTO();
        exchangeRateDataDTO.setExchangeRate(expected);

        List<ExchangeRateDataDTO> data = new ArrayList<>(List.of(exchangeRateDataDTO));


        exchangeRateResponseDTO.setData(data);

        when(restTemplate.getForObject(any(URI.class), eq(ExchangeRateResponseDTO.class))).thenReturn(exchangeRateResponseDTO);


        DateIntervalDTO dateIntervalDTO = service.generateAcceptableInterval(LocalDate.now());

        BigDecimal result = service.getNewestExchangeRateInInterval(COUNTRY_BRAZIL, CURRENCEY_REAL, dateIntervalDTO);
        Assertions.assertEquals(0, expected.compareTo(result));

    }

    @Test
    public void testGetNewestExchangeRateInIntervalReturnNull() {

        when(restTemplate.getForObject(any(URI.class), eq(ExchangeRateResponseDTO.class))).thenReturn(null);

        DateIntervalDTO dateIntervalDTO = service.generateAcceptableInterval(LocalDate.now());

        BigDecimal result = service.getNewestExchangeRateInInterval(COUNTRY_BRAZIL, CURRENCEY_REAL, dateIntervalDTO);
        Assertions.assertEquals(null, result);

    }


}
