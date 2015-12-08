package aww.delp.clients;

import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import aww.delp.models.yelp.Business;

public abstract class YelpResponseHandler {

    public void onFailure(int statusCode, JSONObject error) {
        Log.e(this.getClass().getName(), "Error calling Yelp API: " + statusCode);
    }

    public static abstract class Businesses extends YelpResponseHandler {
        public abstract void onSuccess(List<Business> businesses);
    }

    public static abstract class SingleBusiness extends YelpResponseHandler {
        public abstract void onSuccess(Business business);
    }
}
