package aww.delp.models;

import android.util.Log;

import com.activeandroid.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel extends Model {
    protected static ObjectMapper mapper = new ObjectMapper();

    protected static <T extends BaseModel> T fromJson(JSONObject jsonObject, Class<T> type) {
        try {
            return mapper.readValue(jsonObject.toString(), type);
        } catch (IOException e) {
            Log.e(type.getName(), String.format("Failed parsing Groupon response: [%s] %s", e.getMessage(),jsonObject.toString()));
        }
        return null;
    }

    protected static <T extends BaseModel> List<T> fromJson(JSONArray jsonArray, Class<T> type) {
        List<T> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            T element = fromJson(jsonArray.optJSONObject(i), type);
            if(element != null) {
                list.add(element);
            }
        }
        return list;
    }

    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toString(List<? extends BaseModel> models) {
        StringBuilder sb = new StringBuilder();
        for(BaseModel model : models) {
            sb.append(model.toString()).append("\n");
        }
        return sb.toString();
    }
}
