package aww.delp.models.groupon;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import aww.delp.models.BaseModel;

public class DealOptions extends BaseModel {
    @JsonProperty
    String id;

    public static DealOptions fromJson(JSONObject deal) {
        return BaseModel.fromJson(deal, DealOptions.class);
    }

    public static List<DealOptions> fromJson(JSONArray deals) {
        return BaseModel.fromJson(deals, DealOptions.class);
    }
}
