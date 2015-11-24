package aww.delp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import aww.delp.models.groupon.Division;

public class Preferences {
    private static final String KEY_DIVISION_ID = "division_id";

    private SharedPreferences preferences;
    private Preferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    private static Preferences instance = null;

    public static Preferences getInstance(Context context) {
        if(instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }

    public Division getDivision() {
        String id = preferences.getString(KEY_DIVISION_ID, null);
        if(id != null) {
            return Division.getByGrouponId(id);
        }
        return null;
    }

    public void setDivision(Division division) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(KEY_DIVISION_ID, division.getGrouponId());
        edit.commit();
        Log.i(getClass().getName(),"division saved: " + division.getGrouponId());
    }
}
