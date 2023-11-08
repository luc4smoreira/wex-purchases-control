package mirand.lucas.wexpurchasescontrol.model.services.imp;

import mirand.lucas.wexpurchasescontrol.model.services.CurrencyExchangeRateService;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

public class TreasureGovExchangeRateServiceImp implements CurrencyExchangeRateService {

    private final String BASE_URL = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";


    public URI buildExchangeRateUrl(String startDate, String endDate, String currency) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("filter", String.format("record_date:lte:%s,record_date:gte:%s,currency:eq:%s", endDate, startDate, currency))
                .queryParam("fields", "record_date,country,currency,exchange_rate")
                .queryParam("sort", "record_date")
                .queryParam("limit", 1)
                .build()
                .toUri();
    }

    @Override
    public BigDecimal getCurrencyExchangeRateByDate(String currency, LocalDate date) {
        //TODO
        return null;
    }
}
