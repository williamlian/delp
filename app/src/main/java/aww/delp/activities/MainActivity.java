package aww.delp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import aww.delp.Delp;
import aww.delp.Preferences;
import aww.delp.R;
import aww.delp.adaptors.DealCardAdaptor;
import aww.delp.clients.GrouponResponseHandler;
import aww.delp.helpers.DealYelpMatcher;
import aww.delp.helpers.GrouponRestaurantLoader;
import aww.delp.models.groupon.Deal;
import aww.delp.models.groupon.Division;
import aww.delp.models.yelp.Business;

public class MainActivity extends AppCompatActivity
        implements GrouponRestaurantLoader.Handler
{

    /**********************************************************************************************
     *
     * Activity Methods
     *
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_deals);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        deals = new ArrayList<Deal>();
        mAdapter = new DealCardAdaptor(this, deals);
        mRecyclerView.setAdapter(mAdapter);

        dealYelpMatcher = new DealYelpMatcher(this);

        loadDivision();
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
    /**********************************************************************************************
     *
     * Data Loader
     *
     **********************************************************************************************/
    public void loadDeals() {
        Preferences preferences = Delp.getPreferences();
        loader = new GrouponRestaurantLoader(this, deals, preferences.getDivision(), this);
        loader.loadNew(null);
    }

    public void loadDivision() {
        final Preferences preferences = Delp.getPreferences();
        final Division division = preferences.getDivision();
        if (division == null) {
            Delp.getGrouponClient().getDivisions(new GrouponResponseHandler.Divisions() {
                @Override
                public void onSuccess(List<Division> divisions) {
                    Division.refresh(divisions);
                    preferences.setDivision(divisions.get(0));
                    Log.i(getClass().getName(), "Setting first time division: " + divisions.get(0).getName());
                    loadDeals();
                }
            });
        } else {
            loadDeals();
        }
    }

    @Override
    public void onLoadGrouponRestaurantCompleted(boolean success) {
        mAdapter.notifyDataSetChanged();
    }

    /**********************************************************************************************
     *
     * Private Members
     *
     **********************************************************************************************/
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Deal> deals;
    private Division division;
    private DealYelpMatcher dealYelpMatcher;

    private GrouponRestaurantLoader loader;
}
