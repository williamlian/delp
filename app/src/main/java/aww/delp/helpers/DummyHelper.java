package aww.delp.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * helper to get dummy client response
 */
public class DummyHelper {

    public static JSONObject readJsonFile(Context context, String fileName) {
        Log.i(DummyHelper.class.getName(), "Loading dummy file " + fileName);
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }
            JSONObject json = new JSONObject(sb.toString());
            return json;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static JSONObject getDummyDeals(Context context) {
        return readJsonFile(context, "dummy/deals.json");
    }

    public static JSONObject getDummyDivisions(Context context) {
        return readJsonFile(context, "dummy/divisions.json");
    }

    public static JSONObject getDummyDeal(Context context, String uuid) {
        JSONArray deals = getDummyDeals(context).optJSONArray("deals");
        for(int i = 0; i < deals.length(); i++) {
            if(deals.optJSONObject(i).optString("uuid").equals(uuid)) {
                return deals.optJSONObject(i);
            }
        }
        return null;
    }
}
