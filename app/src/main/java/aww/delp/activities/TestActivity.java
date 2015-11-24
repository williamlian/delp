package aww.delp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import aww.delp.Delp;
import aww.delp.Preferences;
import aww.delp.R;
import aww.delp.clients.Groupon;
import aww.delp.clients.GrouponResponseHandler;
import aww.delp.clients.Yelp;
import aww.delp.clients.YelpResponseHandler;
import aww.delp.models.groupon.Deal;
import aww.delp.models.groupon.Division;
import aww.delp.models.yelp.Business;

public class TestActivity extends AppCompatActivity {
    TextView tv_response;
    Groupon groupon;
    Yelp yelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv_response = (TextView)findViewById(R.id.tv_response);
        groupon = Delp.getGrouponClient();
        yelp = Delp.getYelpClient();
    }

    public void grouponDivisionClicked(View view) {
        groupon.getDivisions(new GrouponResponseHandler.Divisions() {
            @Override
            public void onSuccess(List<Division> division) {
                tv_response.setText(Division.toString(division));
                Division.refresh(division);
            }
        });
    }

    public void grouponDealsClicked(View view) {
        final Preferences preferences = Delp.getPreferences();
        final Division division = preferences.getDivision();
        if(division == null) {
            groupon.getDivisions(new GrouponResponseHandler.Divisions() {
                @Override
                public void onSuccess(List<Division> divisions) {
                    Division.refresh(divisions);
                    preferences.setDivision(divisions.get(0));
                    Log.i(getClass().getName(),"Setting first time division: " + divisions.get(0).getName());
                    getDeals(divisions.get(0).getUuid());
                }
            });
        } else {
            getDeals(division.getUuid());
        }

    }

    private void getDeals(String divisionId) {
        groupon.getDeals(divisionId, 0, 20, Groupon.CHANNEL_LOCAL, new GrouponResponseHandler.Deals() {
            @Override
            public void onSuccess(List<Deal> deals) {
                tv_response.setText(Deal.toString(deals));
            }
        });
    }

    public void yelpBusinessClicked(View view) {
        yelp.searchForBusinessesByLocation("dinner", "Seattle, WA", 10, new YelpResponseHandler.Businesses() {
            @Override
            public void onSuccess(List<Business> businesses) {
                tv_response.setText(Business.toString(businesses));
            }
        });
    }

    public void yelpBusinessIdClicked(View view) {
    }
}
