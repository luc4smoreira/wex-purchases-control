package miranda.lucas.wexpurchasescontrol.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponseDTO {

    @JsonProperty("data")
    private List<ExchangeRateDataDTO> data;

    @JsonProperty("meta")
    private ExchangeRateMeta meta;

    public List<ExchangeRateDataDTO> getData() {
        return data;
    }

    public void setData(List<ExchangeRateDataDTO> data) {
        this.data = data;
    }

    public ExchangeRateMeta getMeta() {
        return meta;
    }

    public void setMeta(ExchangeRateMeta meta) {
        this.meta = meta;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ExchangeRateMeta {
    @JsonProperty("count")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}


