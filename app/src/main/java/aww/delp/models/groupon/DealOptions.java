package aww.delp.models.groupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import aww.delp.models.BaseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DealOptions extends BaseModel {
    @JsonProperty("uuid")
    String uuid;

    @JsonProperty("discountPercent")
    String discountPercent;

    @JsonProperty("discount")
    Price discount;

    @JsonProperty("price")
    Price price;

    @JsonProperty("value")
    Price value;

    public static DealOptions fromJson(JSONObject deal) {
        return BaseModel.fromJson(deal, DealOptions.class);
    }

    public static List<DealOptions> fromJson(JSONArray deals) {
        return BaseModel.fromJson(deals, DealOptions.class);
    }

    public String getUuid() {
        return uuid;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public Price getDiscount() {
        return discount;
    }

    public Price getPrice() {
        return price;
    }

    public Price getValue() {
        return value;
    }
}
