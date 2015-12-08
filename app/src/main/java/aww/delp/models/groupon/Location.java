package aww.delp.models.groupon;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Parcelable {

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
    Double lat;

    @JsonProperty("lng")
    Double lng;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(streetAddress1);
        if(streetAddress2 != null) {
            sb.append(", ").append(streetAddress2);
        }
        sb.append(", ").append(city).append(", ").append(state).append(" ").append(postalCode);
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.streetAddress1);
        dest.writeString(this.streetAddress2);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.neibhorhood);
        dest.writeString(this.postalCode);
        dest.writeString(this.country);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.uuid);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
    }

    public Location() {
    }

    protected Location(Parcel in) {
        this.name = in.readString();
        this.streetAddress1 = in.readString();
        this.streetAddress2 = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.neibhorhood = in.readString();
        this.postalCode = in.readString();
        this.country = in.readString();
        this.phoneNumber = in.readString();
        this.uuid = in.readString();
        this.lat = (Double) in.readValue(Long.class.getClassLoader());
        this.lng = (Double) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
