package aww.delp.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import aww.delp.clients.Groupon;
import aww.delp.clients.GrouponResponseHandler;
import aww.delp.models.groupon.Deal;
import aww.delp.models.groupon.Division;

/*
 * This class helps to load GAPI deal into a list
 * - it keeps track of the loaded deals
 * - it implements loadMore() to support scroll-to-load
 * - it saves query so that loadMore() is consistent
 * - it detects no more data
 */
public class GrouponRestaurantLoader {

    //this is the callback to notify if the load has add more data to the list
    public interface Handler {
        void onLoadGrouponRestaurantCompleted(boolean dataChanged);
    }

    private int offset = 0;
    private List<Deal> store;
    private String lastError = null;
    private Groupon client;
    private Handler handler;
    private Division division;
    private String query;
    private boolean noMoreData;

    private static final int LOAD_SIZE = 20;
    private static final String CHANNEL = Groupon.CHANNEL_LOCAL;
    private static final int MAX_SIZE = 200;
    private static final String DEAL_FILTER = "category:food-and-drink";

    public GrouponRestaurantLoader(Context context, List<Deal> store, Division division, Handler handler) {
        this.store = store;
        this.division = division;
        this.offset = 0;
        this.handler = handler;
        this.noMoreData = false;
        client = Groupon.getInstance(context);
    }

    public void loadNew(String query) {
        this.query = query;
        this.offset = 0;
        client.searchDeals(division.getUuid(), offset, LOAD_SIZE, CHANNEL, DEAL_FILTER, query, new GrouponResponseHandler.Deals() {
            @Override
            public void onSuccess(List<Deal> deals) {
                //for(Deal d:deals){Log.i(getClass().getName(),d.getGrouponId());}
                store.clear();
                offset = deals.size();
                lastError = null;
                store.addAll(deals);
                noMoreData = false;
                Log.i(this.getClass().getName(), String.format("%d deals loaded", offset));
                handler.onLoadGrouponRestaurantCompleted(true);
            }

            @Override
            public void onFailure(int statusCode, JSONObject error) {
                noMoreData = true;
                lastError = String.format("Load New Groupon Deal Failed. [%d] %s", statusCode, error.toString());
                handler.onLoadGrouponRestaurantCompleted(false);
            }
        });
    }

    public void loadMore() {
        if (noMoreData) {
            handler.onLoadGrouponRestaurantCompleted(false);
        } else if (offset >= MAX_SIZE) {
            noMoreData = true;
            handler.onLoadGrouponRestaurantCompleted(false);
        } else if (lastError != null) {
            //once we got error, we don't load any more
            handler.onLoadGrouponRestaurantCompleted(false);
        } else {
            client.searchDeals(division.getUuid(), offset, LOAD_SIZE, CHANNEL, DEAL_FILTER, query, new GrouponResponseHandler.Deals() {
                @Override
                public void onSuccess(List<Deal> deals) {
                    if(deals != null && deals.size() > 0) {
                        offset += deals.size();
                        store.addAll(deals);
                        lastError = null;
                        handler.onLoadGrouponRestaurantCompleted(true);
                    } else {
                        noMoreData = true;
                        handler.onLoadGrouponRestaurantCompleted(false);
                    }
                }

                @Override
                public void onFailure(int statusCode, JSONObject error) {
                    lastError = String.format("Load More Groupon Deal Failed. [%d] %s", statusCode, error.toString());
                    noMoreData = true;
                    handler.onLoadGrouponRestaurantCompleted(false);
                }
            });
        }
    }

    public boolean hasError() {
        return lastError != null;
    }

    public String getLastError() {
        return lastError;
    }
}
