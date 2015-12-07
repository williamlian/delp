package aww.delp.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import aww.delp.clients.Yelp;
import aww.delp.clients.YelpResponseHandler;
import aww.delp.models.DealBusinessMatch;
import aww.delp.models.groupon.Deal;
import aww.delp.models.yelp.Business;

public class DealBusinessMatcher {
    Context context;
    Yelp client;

    public interface Handler {
        void onMatchYelpBusinessCompleted(Deal deal, Business business);
    }

    public DealBusinessMatcher(Context context) {
        this.context = context;
        this.client = Yelp.getInstance(context);
    }

    public void matchDeal(final Deal deal, final Handler handler) {
        String businessId = DealBusinessMatch.getBusinessId(deal.getUuid());
        if(businessId != null) {
            client.getBusiness(businessId, new YelpResponseHandler.SingleBusiness() {
                @Override
                public void onSuccess(Business business) {
                    handler.onMatchYelpBusinessCompleted(deal, business);
                }
                @Override
                public void onFailure(int statusCode, JSONObject error) {
                    super.onFailure(statusCode, error);
                    handler.onMatchYelpBusinessCompleted(deal, null);
                }
            });
            return;
        }
        String query = deal.getMerchant().getName();
        String location;
        try {
            location = deal.getFirstOption().getFirstLocation().toString();
            Log.i(getClass().getName(), String.format("Matching deal %s @ %s", query, location));
        } catch (Exception e) {
            handler.onMatchYelpBusinessCompleted(deal, null);
            return;
        }

        client.searchForBusinessesByLocation(query, location, 1, new YelpResponseHandler.Businesses() {
            @Override
            public void onSuccess(List<Business> businesses) {
                if(businesses.size() > 0) {
                    Business business = businesses.get(0);
                    DealBusinessMatch.setBusinessId(deal.getUuid(), business.getBusinessId());
                    handler.onMatchYelpBusinessCompleted(deal, business);
                }
                else {
                    handler.onMatchYelpBusinessCompleted(deal, null);
                }
            }

            @Override
            public void onFailure(int statusCode, JSONObject error) {
                super.onFailure(statusCode, error);
                handler.onMatchYelpBusinessCompleted(deal, null);
            }
        });
    }

}
