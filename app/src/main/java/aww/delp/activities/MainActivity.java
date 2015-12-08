package aww.delp.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import aww.delp.helpers.DealBusinessMatcher;
import aww.delp.helpers.GrouponRestaurantLoader;
import aww.delp.models.groupon.Deal;
import aww.delp.models.groupon.Division;

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

        dealBusinessMatcher = new DealBusinessMatcher(this);

        loadDeals();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
    public void search(String query) {
        if(!loading) {
            loading = true;
            Log.i(this.getClass().getName(), "Searching GAPI " + query);
            loader.loadNew(query);
        }
    }

    public void loadDeals() {
        if(!loading) {
            loading = true;
            Preferences preferences = Delp.getPreferences();
            loader = new GrouponRestaurantLoader(this, deals, preferences.getDivision(), this);
            loader.loadNew(null);
        }
    }

    @Override
    public void onLoadGrouponRestaurantCompleted(boolean success) {
        loading = false;
        if(success) {
            mAdapter.notifyDataSetChanged();
            Log.i(this.getClass().getName(), "Adaptor reloaded " + mAdapter.getItemCount());
        } else {
            Log.e(this.getClass().getName(), loader.getLastError());
        }
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
    private DealBusinessMatcher dealBusinessMatcher;

    private boolean loading;

    private GrouponRestaurantLoader loader;
}
