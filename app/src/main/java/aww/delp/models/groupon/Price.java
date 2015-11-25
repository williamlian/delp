package aww.delp.models.groupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {
    @JsonProperty("amount")
    Long amount;

    @JsonProperty("currencyCode")
    String currencyCode;

    @JsonProperty("formattedAmount")
    String formattedAmount;

    public String toString() {
        return formattedAmount;
    }
}
