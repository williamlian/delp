package aww.delp.clients;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import aww.delp.R;
import aww.delp.helpers.DummyHelper;
import aww.delp.models.groupon.Deal;
import aww.delp.models.groupon.Division;

public class Groupon {
    public static final String CHANNEL_LOCAL = "local";
    /***********************************************************************
     * Configurations
     **********************************************************************/
    protected final String BASE_URL = "https://api.groupon.com/v2";
    protected final String CLIENT_ID;

    public static Groupon getInstance(Context context) {
        if (instance == null) {
            instance = new Groupon(context);
        }
        return instance;
    }

    /***********************************************************************
     * Endpoints
     **********************************************************************/
    //https://sites.google.com/a/groupon.com/api-internal/api-resources/deals#index-request
    public void getDeals(
            String division_id,
            Integer offset,
            Integer limit,
            String channel,
            final GrouponResponseHandler.Deals handler)
    {
        if(isDummy()) {
            handler.onSuccess(getDummyDeals());
            return;
        }
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        params.put("division_id", division_id);
        if(channel != null)
            params.put("channel_id", channel);
        params.put("offset", offset);
        params.put("limit", limit);
        client.get(getUrl("/deals"), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                handler.onSuccess(Deal.fromJson(response.optJSONArray("deals")));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handler.onFailure(statusCode, errorResponse);
            }
        });
    }

    //https://sites.google.com/site/grouponapiv2/api-resources/divisions
    public void getDivisions(Double lat, Double lng, final GrouponResponseHandler.Divisions handler) {
        if(isDummy()) {
            handler.onSuccess(getDummyDivisions());
            return;
        }
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        params.put("show", "default");
        if(lat != null && lng != null) {
            params.put("sort", "distance");
            params.put("lat", lat);
            params.put("lng", lng);
        }
        client.get(getUrl("/divisions"), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                handler.onSuccess(Division.fromJson(response.optJSONArray("divisions")));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handler.onFailure(statusCode, errorResponse);
            }
        });
    }

    //https://sites.google.com/a/groupon.com/api-internal/api-resources/deals/search
    public void searchDeals(String division_id,
                            Integer offset,
                            Integer limit,
                            String channel,
                            String filter,
                            String query,
                            final GrouponResponseHandler.Deals handler) {
        if(isDummy()) {
            handler.onSuccess(getDummyDeals());
            return;
        }
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        params.put("division_id", division_id);
        if(channel != null)
            params.put("channel_id", channel);
        if(filter != null)
            params.put("filters", filter);
        if(query != null)
            params.put("query", URLEncoder.encode(query));
        params.put("offset", offset);
        params.put("limit", limit);
        Log.i(getClass().getName(), params.toString());
        client.get(getUrl("/deals/search"), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                handler.onSuccess(Deal.fromJson(response.optJSONArray("deals")));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handler.onFailure(statusCode, errorResponse);
            }
        });
    }

    //https://sites.google.com/a/groupon.com/api-internal/api-resources/deals#show-request
    public void showDeal(String uuid, final GrouponResponseHandler.SingleDeal handler) {
        if(isDummy()) {
            handler.onSuccess(getDummyDeal(uuid));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        client.get(getUrl("/deals/" + uuid), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                handler.onSuccess(Deal.fromJson(response.optJSONObject("deal")));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handler.onFailure(statusCode, errorResponse);
            }
        });
    }


    /**********************************************************************************************
     *
     * Private Members
     *
     **********************************************************************************************/
    protected AsyncHttpClient client = new AsyncHttpClient();
    protected static Groupon instance = null;
    private Context context;

    protected Groupon(Context context) {
        CLIENT_ID = context.getString(R.string.groupon_client_id);
        this.context = context;
    }
    protected String getUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    /**********************************************************************************************
     *
     * Dummy
     *
     **********************************************************************************************/
    protected boolean isDummy() {
        return CLIENT_ID.equals("DUMMY");
    }
    protected List<Deal> getDummyDeals() {
        return Deal.fromJson(DummyHelper.getDummyDeals(context).optJSONArray("deals"));
    }
    protected Deal getDummyDeal(String uuid) {
        return Deal.fromJson(DummyHelper.getDummyDeal(context, uuid));
    }
    protected List<Division> getDummyDivisions() {
        return Division.fromJson(DummyHelper.getDummyDivisions(context).optJSONArray("divisions"));
    }
}
