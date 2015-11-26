package aww.delp.helpers;

import android.content.Context;

import org.json.JSONObject;

import java.util.List;

import aww.delp.clients.Yelp;
import aww.delp.clients.YelpResponseHandler;
import aww.delp.models.groupon.Deal;
import aww.delp.models.yelp.Business;

public class DealYelpMatcher {
    Context context;
    Yelp client;

    public interface Handler {
        void onMatchYelpBusinessCompleted(Deal deal, Business business);
    }

    public DealYelpMatcher(Context context) {
        this.context = context;
        this.client = Yelp.getInstance(context);
    }

    public void matchDeal(final Deal deal, final Handler handler) {
        String query = deal.getMerchant().getName();
        String location;
        try {
            location = deal.getFirstOption().getFirstLocation().toString();
        } catch (Exception e) {
            handler.onMatchYelpBusinessCompleted(deal, null);
            return;
        }

        client.searchForBusinessesByLocation(query, location, 1, new YelpResponseHandler.Businesses() {
            @Override
            public void onSuccess(List<Business> businesses) {
                if(businesses.size() > 0)
                    handler.onMatchYelpBusinessCompleted(deal, businesses.get(0));
                else
                    handler.onMatchYelpBusinessCompleted(deal, null);
            }

            @Override
            public void onFailure(int statusCode, JSONObject error) {
                super.onFailure(statusCode, error);
                handler.onMatchYelpBusinessCompleted(deal, null);
            }
        });
    }

}
