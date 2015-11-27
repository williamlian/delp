package aww.delp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import aww.delp.Delp;
import aww.delp.R;
import aww.delp.clients.Groupon;
import aww.delp.clients.GrouponResponseHandler;
import aww.delp.clients.Yelp;
import aww.delp.helpers.DealBusinessMatcher;
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

        ivDetailImage = (ImageView)findViewById(R.id.ivDetailImage);
        tvDetailDescription = (TextView)findViewById(R.id.tvDetailDescription);
        tvDetailLocation = (TextView)findViewById(R.id.tvDetailLocation);
        ivDetailPrice = (ImageView)findViewById(R.id.ivDetailPrice);
        ivDetailRating = (ImageView)findViewById(R.id.ivDetailRating);

        Picasso.with(this).load(deal.getLargeImageUrl()).into(ivDetailImage);
        tvDetailDescription.setText(deal.getHighlightsHtml());
        tvDetailLocation.setText(deal.getFirstOption().getFirstLocation().toString());
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

        DealBusinessMatcher matcher = new DealBusinessMatcher(this);
        matcher.matchDeal(deal, new DealBusinessMatcher.Handler() {
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

    private ImageView ivDetailImage;
    private TextView tvDetailDescription;
    private TextView tvDetailLocation;
    private ImageView ivDetailPrice;
    private ImageView ivDetailRating;
}
