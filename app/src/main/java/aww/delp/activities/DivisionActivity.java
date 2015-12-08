package aww.delp.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aww.delp.Delp;
import aww.delp.R;
import aww.delp.clients.Groupon;
import aww.delp.clients.GrouponResponseHandler;
import aww.delp.models.groupon.Division;

public class DivisionActivity extends AppCompatActivity implements LocationListener {

    private static final boolean DEBUG_MODE = true;

    private ListView lv_divisions;
    private ArrayAdapter<String> divisionAdaptor;
    private TextView tv_whereAreYou;
    private List<Division> divisions;

    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        lv_divisions = (ListView)findViewById(R.id.lv_divisions);
        divisionAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv_divisions.setAdapter(divisionAdaptor);
        lv_divisions.setOnItemClickListener(onDivisionClickedListener);

        tv_whereAreYou = (TextView)findViewById(R.id.tv_whereAreYou);

        lv_divisions.setVisibility(View.GONE);
        tv_whereAreYou.setVisibility(View.GONE);

        if(!DEBUG_MODE && Delp.getPreferences().getDivision() != null) {
           startMain();
        } else {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location;
            try {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
                    loadDivisions(location.getLatitude(), location.getLongitude());
                }
                else {
                    Log.i(this.getClass().getName(), "Requesting GPS update");
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20L, 10F, this);
                }
            } catch (SecurityException se) {
                Log.e(this.getClass().getName(), se.getStackTrace().toString());
                loadDivisions(null, null);
            }
        }
    }

    private void loadDivisions(Double lat, Double lng) {
        Groupon client = Delp.getGrouponClient();
        client.getDivisions(lat, lng, onDivisionsLoaded);
    }

    private AdapterView.OnItemClickListener onDivisionClickedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Division selectedDivision = divisions.get(position);
            Log.i(this.getClass().getName(), "user selected division " + selectedDivision.getName() + " " + selectedDivision.getUuid());
            Delp.getPreferences().setDivision(selectedDivision);
            startMain();
        }
    };

    private GrouponResponseHandler.Divisions onDivisionsLoaded = new GrouponResponseHandler.Divisions() {
        @Override
        public void onSuccess(List<Division> divisions) {
            DivisionActivity.this.divisions = divisions;
            List<String> divisionStringList = new ArrayList<>();
            for(Division division : divisions) {
                divisionStringList.add(division.getName());
            }
            divisionAdaptor.addAll(divisionStringList);
            lv_divisions.setVisibility(View.VISIBLE);
            tv_whereAreYou.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(int statusCode, JSONObject error) {
            super.onFailure(statusCode, error);
            Toast.makeText(DivisionActivity.this, "Sorry we cannot load API", Toast.LENGTH_SHORT).show();
        }
    };

    private void startMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.i("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            try {
                mLocationManager.removeUpdates(this);
            } catch(SecurityException se) {
            }
            loadDivisions(location.getLatitude(), location.getLongitude());
        } else {
            loadDivisions(null, null);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
