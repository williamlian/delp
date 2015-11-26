package aww.delp.clients;

import android.content.Context;

import com.codepath.oauth.ScribeRequestAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import java.util.List;

import aww.delp.R;
import aww.delp.helpers.DummyHelper;
import aww.delp.models.yelp.Business;

public class Yelp {
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    /**********************************************************************************************
     *
     * Configurations
     *
     *********************************************************************************************/
    public static final Class<? extends Api> REST_API_CLASS = TwoStepOAuth.class;
    private static final String REST_URL = "https://api.yelp.com";

    private final String CONSUMER_KEY;
    private final String CONSUMER_SECRET;

    public static Yelp getInstance(Context context) {
        if(instance == null) {
            instance = new Yelp(context);
        }
        return instance;
    }

    /**********************************************************************************************
     *
     * Endpoints
     *
     *********************************************************************************************/
    // https://www.yelp.com/developers/documentation/v2/search_api
    public void searchForBusinessesByLocation(String query, String location, int limit, final YelpResponseHandler.Businesses handler) {
        if(isDummy()) {
            handler.onSuccess(getDummyBusinesses(query));
            return;
        }
        String apiUrl = getApiUrl(SEARCH_PATH);
        RequestParams params = new RequestParams();
        params.put("term", query);
        params.put("location", location);
        params.put("limit", String.valueOf(limit));
        client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                handler.onSuccess(Business.fromJson(response.optJSONArray("businesses")));
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
     *********************************************************************************************/
    protected static Yelp instance;
    private Token token;
    private YelpAsyncHttpClient client;
    private Context context;

    protected Yelp(Context context) {
        this.CONSUMER_KEY = context.getString(R.string.yelp_consumer_key);
        this.CONSUMER_SECRET = context.getString(R.string.yelp_consumer_secret);
        this.client = new YelpAsyncHttpClient(REST_API_CLASS, CONSUMER_KEY, CONSUMER_SECRET);
        token = new Token(context.getString(R.string.yelp_token), context.getString(R.string.yelp_token_secret));
        this.client.setAccessToken(token);
        this.context = context;
    }

    protected String getApiUrl(String path) {
        return REST_URL + path;
    }

    private static class YelpAsyncHttpClient extends AsyncHttpClient {
        private Class<? extends Api> apiClass;
        private Token accessToken;
        private OAuthService service;

        public YelpAsyncHttpClient(Class<? extends Api> apiClass, String consumerKey, String consumerSecret) {
            this.apiClass = apiClass;
            this.service = (new ServiceBuilder()).provider(apiClass).apiKey(consumerKey).apiSecret(consumerSecret).build();
        }

        public void setAccessToken(Token accessToken) {
            this.accessToken = accessToken;
        }

        protected RequestHandle sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, ResponseHandlerInterface responseHandler, Context context) {
            if(this.service != null && this.accessToken != null) {
                try {
                    ScribeRequestAdapter e = new ScribeRequestAdapter(uriRequest);
                    this.service.signRequest(this.accessToken, e);
                    return super.sendRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else if(this.accessToken == null) {
                throw new OAuthException("Cannot send unauthenticated requests. Please attach an access token!");
            } else {
                throw new OAuthException("Cannot send unauthenticated requests for undefined service. Please specify a valid api service!");
            }
        }
    }

    /**********************************************************************************************
     *
     * Dummy
     *
     **********************************************************************************************/
    protected boolean isDummy() {
        return CONSUMER_KEY.equals("DUMMY");
    }

    protected List<Business> getDummyBusinesses(String query) {
        return Business.fromJson(DummyHelper.getDummyBusinesses(context, query).optJSONArray("businesses"));
    }

}
