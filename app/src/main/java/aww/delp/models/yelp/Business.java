package aww.delp.models.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import aww.delp.models.BaseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Business extends BaseModel{

    @JsonProperty("display_phone")
    String displayPhone;

    @JsonProperty("id")
    String businessId;

    @JsonProperty("image_url")
    String imageUrl;

    @JsonProperty("name")
    String name;

    @JsonProperty("rating")
    String rating;

    @JsonProperty("url")
    String url;

    @JsonProperty("rating_img_url_small")
    String ratingImageUrl;


    public static Business fromJson(JSONObject business) {
        return BaseModel.fromJson(business, Business.class);
    }

    public static List<Business> fromJson(JSONArray businesses) {
        return BaseModel.fromJson(businesses, Business.class);
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return "Yelp " + rating + " Stars";
    }

    public String getUrl() {
        return url;
    }

    public String getRatingImageUrl() {return ratingImageUrl; }
}
