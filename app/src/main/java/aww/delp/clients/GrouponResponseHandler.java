package aww.delp.clients;

import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import aww.delp.models.groupon.Deal;
import aww.delp.models.groupon.Division;

public abstract class GrouponResponseHandler {

    public void onFailure(int statusCode, JSONObject error) {
        Log.e(this.getClass().getName(),"Error calling Groupon API: " + error.toString());
    }

    public static abstract class Deals extends GrouponResponseHandler {
        public abstract void onSuccess(List<aww.delp.models.groupon.Deal> deals);
    }

    public static abstract class SingleDeal extends GrouponResponseHandler {
        public abstract void onSuccess(Deal deal);
    }

    public static abstract class Divisions extends GrouponResponseHandler {
        public abstract void onSuccess(List<Division> division);
    }
}
