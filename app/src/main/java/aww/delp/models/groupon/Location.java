package aww.delp.models.groupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

    @JsonProperty("name")
    String name;

    @JsonProperty("streetAddress1")
    String streetAddress1;

    @JsonProperty("streetAddress2")
    String streetAddress2;

    @JsonProperty("state")
    String state;

    @JsonProperty("city")
    String city;

    @JsonProperty("neighborhood")
    String neibhorhood;

    @JsonProperty("postalCode")
    String postalCode;

    @JsonProperty("country")
    String country;

    @JsonProperty("phoneNumber")
    String phoneNumber;

    @JsonProperty("uuid")
    String uuid;

    @JsonProperty("lat")
    Long lat;

    @JsonProperty("lng")
    Long lng;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(streetAddress1);
        if(streetAddress2 != null) {
            sb.append(", ").append(streetAddress2);
        }
        sb.append(", ").append(city).append(", ").append(state).append(" ").append(postalCode);
        return sb.toString();
    }
}
