package mirand.lucas.wexpurchasescontrol.model.services.imp;

import mirand.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangeRateDataDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangeRateResponseDTO;
import mirand.lucas.wexpurchasescontrol.model.services.CurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

@Service
public class TreasureGovExchangeRateServiceImp implements CurrencyExchangeRateService {


    private static final int MAX_EXCHANGE_RATE_ACCEPTABLE_IN_MONTHS = 6;
    private static final String BASE_URL = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";


    private final RestTemplate restTemplate;

    @Autowired
    public TreasureGovExchangeRateServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private URI buildExchangeRateUrl(String country, String currency, DateIntervalDTO intervalDTO) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("filter", String.format("record_date:lte:%s,record_date:gte:%s,country:eq:%s,currency:eq:%s", intervalDTO.getEnd(), intervalDTO.getStart(), country, currency))
                .queryParam("fields", "record_date,country,currency,exchange_rate")
                .queryParam("sort", "-record_date")
                .queryParam("limit", 1)
                .build()
                .toUri();
    }

    @Override
    public BigDecimal getNewestExchangeRateInInterval(String country, String currency, DateIntervalDTO intervalDTO) {
        BigDecimal exchangeRate = null;

        URI url = buildExchangeRateUrl(country, currency, intervalDTO);

        ExchangeRateResponseDTO response = restTemplate.getForObject(url, ExchangeRateResponseDTO.class);
        if (response != null && response.getData() != null && !response.getData().isEmpty()) {
            ExchangeRateDataDTO exchangeRateDataDTO = response.getData().get(0);
            exchangeRate = exchangeRateDataDTO.getExchangeRate();
        }
        return exchangeRate;
    }

    @Override
    public DateIntervalDTO generateAcceptableInterval(LocalDate purchaseDate) {
        DateIntervalDTO intervalDTO = new DateIntervalDTO();

        intervalDTO.setStart(purchaseDate.minusMonths(MAX_EXCHANGE_RATE_ACCEPTABLE_IN_MONTHS));
        intervalDTO.setEnd(purchaseDate);

        return intervalDTO;
    }
}
