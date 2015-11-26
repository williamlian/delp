package aww.delp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import aww.delp.Delp;
import aww.delp.R;
import aww.delp.clients.Groupon;
import aww.delp.clients.GrouponResponseHandler;
import aww.delp.clients.Yelp;
import aww.delp.helpers.DealYelpMatcher;
import aww.delp.models.groupon.Deal;
import aww.delp.models.yelp.Business;

public class DetailsActivity extends AppCompatActivity {
    public static final String ARGS_DEAL_UUID = "uuid";

    /**********************************************************************************************
     *
     * Activity Methods
     *
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String dealUuid = getIntent().getExtras().getString(ARGS_DEAL_UUID);
        grouponClient = Delp.getGrouponClient();
        yelpClient = Delp.getYelpClient();

        loadDeal(dealUuid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateView() {

        //TODO
        Toast.makeText(this, "deal loaded " + deal.getGrouponId(), Toast.LENGTH_SHORT).show();
        if(business == null)
            Toast.makeText(this, "business not found ", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "business loaded " + business.getName(), Toast.LENGTH_SHORT).show();

    }

    /**********************************************************************************************
     *
     * Data Loader
     *
     **********************************************************************************************/
    public void loadDeal(String uuid) {
        grouponClient.showDeal(uuid, new GrouponResponseHandler.SingleDeal() {
            @Override
            public void onSuccess(Deal deal) {
                onDealLoaded(deal);
            }
        });
    }

    private void onDealLoaded(Deal deal) {
        this.deal = deal;

        DealYelpMatcher matcher = new DealYelpMatcher(this);
        matcher.matchDeal(deal, new DealYelpMatcher.Handler() {
            @Override
            public void onMatchYelpBusinessCompleted(Deal deal, Business business) {
                DetailsActivity.this.business = business;

                updateView();
            }
        });
    }

    /**********************************************************************************************
     *
     * Private Members
     *
     **********************************************************************************************/
    private Deal deal;
    private Business business;
    private Groupon grouponClient;
    private Yelp yelpClient;
}
