package aww.delp.models.groupon;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aww.delp.models.BaseModel;

/**
 * Response from GAPI GET Division
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name="divisions")
public class Division extends BaseModel {
    @JsonProperty("uuid")
    @Column(name="uuid")
    String uuid;

    @JsonProperty("id")
    @Column(name="groupon_id")
    String grouponId;

    @JsonProperty("name")
    @Column(name="name")
    String name;

    @JsonProperty("country")
    @Column(name="country")
    String country;

    @JsonProperty("countryCode")
    @Column(name="country_code")
    String countryCode;

    public static Division fromJson(JSONObject division) {
        try {
            return mapper.readValue(division.toString(), Division.class);
        } catch (IOException e) {
            Log.e(Division.class.getName(), "Failed to parse Groupon division: " + division.toString());
        }
        return null;
    }

    public static List<Division> fromJson(JSONArray divisions) {
        List<Division> divisionList = new ArrayList<>();
        for(int i = 0; i < divisions.length(); i++) {
            Division division = Division.fromJson(divisions.optJSONObject(i));
            if(division != null) {
                divisionList.add(division);
            }
        }
        return divisionList;
    }

    public static void refresh(List<Division> divisions) {
        //clear local storage first
        new Delete().from(Division.class).execute();
        //persist all divisions
        for(Division division : divisions) {
            Log.d(Division.class.getName(), "Saving division record: " + division.toString());
            division.save();
        }
    }

    public static Division getByGrouponId(String id) {
        List<Model> models = new Select().from(Division.class).where("groupon_id=?",id).limit(1).execute();
        if(models.size() > 0) {
            return (Division)models.get(0);
        }
        return null;
    }

    public String getUuid() {
        return uuid;
    }

    public String getGrouponId() {
        return grouponId;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
