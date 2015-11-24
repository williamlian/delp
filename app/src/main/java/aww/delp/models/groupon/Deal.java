package aww.delp.models.groupon;

import android.text.Html;
import android.text.Spanned;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import aww.delp.models.BaseModel;

/**
 * Deal Object from GAPI GET /deals
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deal extends BaseModel {
    @JsonProperty("id")
    String grouponId;

    @JsonProperty("uuid")
    String uuid;

    @JsonProperty("dealUrl")
    String dealUrl;

    @JsonProperty("title")
    String title;

    @JsonProperty("highlightsHtml")
    String highlightsHtml;

    @JsonProperty("pitchHtml")
    String pitchHtml;

    @JsonProperty("soldQuantityMessage")
    String soldQuantityMessage;

    @JsonProperty("placeholderUrl")
    String placeholderUrl;
    @JsonProperty("grid4ImageUrl")
    String grid4ImageUrl;
    @JsonProperty("grid6ImageUrl")
    String grid6ImageUrl;
    @JsonProperty("largeImageUrl")
    String largeImageUrl;
    @JsonProperty("mediumImageUrl")
    String mediumImageUrl;
    @JsonProperty("smallImageUrl")
    String smallImageUrl;
    @JsonProperty("sidebarImageUrl")
    String sidebarImageUrl;


    public static Deal fromJson(JSONObject deal) {
        return BaseModel.fromJson(deal, Deal.class);
    }

    public static List<Deal> fromJson(JSONArray deals) {
        return BaseModel.fromJson(deals, Deal.class);
    }

    public String getGrouponId() {
        return grouponId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDealUrl() {
        return dealUrl;
    }

    public String getTitle() {
        return title;
    }

    public Spanned getHighlightsHtml() {
        return Html.fromHtml(highlightsHtml);
    }

    public Spanned getPitchHtml() {
        return Html.fromHtml(pitchHtml);
    }

    public String getSoldQuantityMessage() {
        return soldQuantityMessage;
    }

    public String getPlaceholderUrl() {
        return placeholderUrl;
    }

    public String getGrid4ImageUrl() {
        return grid4ImageUrl;
    }

    public String getGrid6ImageUrl() {
        return grid6ImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public String getSidebarImageUrl() {
        return sidebarImageUrl;
    }
}
